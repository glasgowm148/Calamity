package org.terrier.asp.readers.datasets;

import static com.mongodb.client.model.Filters.eq;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.InternalRow;
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder;
import org.apache.spark.sql.catalyst.encoders.RowEncoder;
import org.apache.spark.sql.catalyst.json.JSONOptions;
import org.apache.spark.sql.catalyst.json.JacksonParser;
import org.apache.spark.sql.catalyst.util.CaseInsensitiveMap;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.unsafe.types.UTF8String;
import org.bson.Document;
import org.terrier.asp.conf.ASPReaderConf;
import org.terrier.asp.interfaces.DatasetReader;
import org.terrier.asp.readers.utility.SchemaAsJson;
import org.terrier.asp.readers.utility.Tweet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import scala.Function1;
import scala.Function2;
import scala.Tuple2;
import scala.collection.Seq;

/**
 * This class represents a dataset of rows that have previously been saved to a MongoDB
 * collection (in json format). This will read the dataset back into memory on the host
 * machine. 
 * @author richardm
 *
 */
public class MongoDBCollection extends DatasetReader {

	/** This is the input tweet file to get the data from */
	public String inputFile;

	/** The Schema for the rows produced by each Reciever */
	public StructType schema;

	// Logger
	static Logger logger;

	public MongoDBCollection(ASPReaderConf conf, SparkSession spark) {
		super(conf, spark);

		logger = LogManager.getLogger(MongoDBCollection.class);

		if (!loadSchemaFromDBAndCheck()) {
			logger.error("Failed to validate the schema loaded from the MongoDB collection and the output types specified in the reader configuration file.");
			System.exit(1);
		}
	}
	
	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("ASPMain").setMaster("local[1]");
		SparkSession sparkSession = SparkSession
				.builder()
				.config(conf)
				//.config("spark.some.config.option", "some-value")
				.getOrCreate();

		sparkSession.sparkContext().setLogLevel("INFO");
		
		MongoDBCollection coll = new MongoDBCollection(new ASPReaderConf("configurationTemplates/readers/datasets/MongoDBCollection.dataset.conf"), sparkSession);
		
		Dataset<Row> dataset = coll.getDataset();
		dataset.show(10);
	}

	protected boolean loadSchemaFromDBAndCheck() {

		// connect to the database and collection
		String host = conf.getVariables().get("host");
		String databaseName = conf.getVariables().get("databaseName");
		String collectionName = conf.getVariables().get("collectionName");

		MongoClient mongoClient = new MongoClient(host);
		MongoDatabase database = null;

		try {
			database = mongoClient.getDatabase(databaseName);
		} catch (Exception e) {
			logger.warn("Request for the output database "+databaseName+" failed.");
			e.printStackTrace();
			System.exit(1);
		}


		try {
			database.createCollection(collectionName);
			logger.info("Created MongoDB Collection  "+databaseName+"."+collectionName);
		} catch (Exception e) {
			logger.info("MongoDB Collection  "+databaseName+"."+collectionName+" exists");
		}


		MongoCollection<Document> collection = database.getCollection(collectionName);

		// request the schema

		Document schemaDocument = collection.find(eq("KeyFrame", "SCHEMA")).first();
		String schemaJSON = schemaDocument.toJson();

		System.err.println(schemaJSON);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {
			SchemaAsJson schemaObject = mapper.readValue(schemaJSON, SchemaAsJson.class);
			
			schema = new StructType();
			for (int i =0; i<schemaObject.getFieldNames().size(); i++) {
				schema =schema.add(schemaObject.getFieldNames().get(i), DataType.fromJson(schemaObject.getFieldDataTypeJson().get(i)));
			}

			String[] fieldNames = schema.fieldNames();

			boolean checkFailed = false;
			for (String expectedFieldName : conf.getGuaranteedOutputFrameTypes()) {
				boolean found = false;
				for (String fieldName : fieldNames) {
					if (fieldName.equalsIgnoreCase(expectedFieldName)) found = true;
				}
				if (!found) {
					checkFailed = true;
					logger.error("When checking the schema as recorded in the database, it is missing the expected field "+expectedFieldName);
				}
			}

			if (checkFailed) {
				mongoClient.close();
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			mongoClient.close();
			return false;
		}

		mongoClient.close();
		return true;
	}

	@Override
	public Dataset<Row> getDataset() {

		logger.info("      Loading Dataset into Memory on Host...");

		List<Row> datasetInMemory = new ArrayList<Row>();


		// check whether we should store meta data about each tweet
		boolean ignoreMetaData = false;
		if (conf.getVariables().containsKey("ignoreMetaData") && conf.getVariables().get("ignoreMetaData").equalsIgnoreCase("true")) {
			logger.info("        The Reader is configured not to save metadata, MetaDataFrame will be empty");
			ignoreMetaData=true;
		}

		int onloadLogEveryNRows = 10000;
		int countSinceLastReport = onloadLogEveryNRows;
		long recordCount = 0;
		if (conf.getVariables().containsKey("onloadLogEveryNRows")) onloadLogEveryNRows = Integer.parseInt(conf.getVariables().get("onloadLogEveryNRows"));

		long loadOnlyFirstNRows = -1;
		if (conf.getVariables().containsKey("loadOnlyFirstNRows") && Long.parseLong(conf.getVariables().get("loadOnlyFirstNRows"))>0) {
			loadOnlyFirstNRows = Long.parseLong(conf.getVariables().get("loadOnlyFirstNRows"));
			logger.info("        The Reader is configured to truncate the dataset, only the first "+loadOnlyFirstNRows+" rows will be read");
			ignoreMetaData=true;
		}

		// connect to the database and collection
		String host = conf.getVariables().get("host");
		String databaseName = conf.getVariables().get("databaseName");
		String collectionName = conf.getVariables().get("collectionName");

		MongoClient mongoClient = new MongoClient(host);
		MongoDatabase database = null;

		try {
			database = mongoClient.getDatabase(databaseName);
		} catch (Exception e) {
			logger.warn("Request for the output database "+databaseName+" failed.");
			e.printStackTrace();
			System.exit(1);
		}


		try {
			database.createCollection(collectionName);
			logger.info("Created MongoDB Collection  "+databaseName+"."+collectionName);
		} catch (Exception e) {
			logger.info("MongoDB Collection  "+databaseName+"."+collectionName+" exists");
		}


		MongoCollection<Document> collection = database.getCollection(collectionName);


		// this is the java equivalent of what happens when you ask spark to load a json dataset from file
		// we are just interested in how a json string gets to an InternalRow
		CaseInsensitiveMap<String> extraOptions = new CaseInsensitiveMap<String>(new scala.collection.immutable.HashMap<String,String>());
		JSONOptions jsonOptions = new JSONOptions(extraOptions, 
				spark.sessionState().conf().sessionLocalTimeZone(),
				spark.sessionState().conf().columnNameOfCorruptRecord());
		JacksonParser parser = new JacksonParser(schema, jsonOptions);
		
		// hack as the original incantation was built in scala and used lambdas
		CreateParser createParser = new CreateParser();
		ConvertUTF8 convertUTF8 = new ConvertUTF8();
		
		// This gets us from an InternalRow to an actual Row
		ExpressionEncoder<Row> encoder = RowEncoder.apply(schema);
	
		
		try {
			FindIterable<Document> documentIterable = collection.find();

			MongoCursor<Document> documentIterator = documentIterable.iterator();
			
			while (documentIterator.hasNext()) {

				Document bson = documentIterator.next();
				
				if (countSinceLastReport==0) {
					logger.info("        Loaded "+recordCount+" records so far");

					countSinceLastReport = onloadLogEveryNRows;
				}
				try {
					
					String json = bson.toJson();
					
					Seq<InternalRow> internalRows =  parser.parse(json, createParser, convertUTF8);

					scala.collection.immutable.List<InternalRow> rowList = internalRows.toList();
					
					Row row = encoder.fromRow(rowList.head());
					
					datasetInMemory.add(row);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				recordCount++;
				countSinceLastReport--;
				if (loadOnlyFirstNRows>0 && recordCount>=loadOnlyFirstNRows) break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return spark.createDataFrame(datasetInMemory, schema);
	}

	@Override
	public StructType getSchema() {
		return schema;
	}
	
	
	public class CreateParser implements Function2<JsonFactory,String,com.fasterxml.jackson.core.JsonParser> {

		@Override
		public com.fasterxml.jackson.core.JsonParser apply(JsonFactory arg0, String arg1) {
			try {
				return arg0.createParser(arg1);
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		public double apply$mcDDD$sp(double arg0, double arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public double apply$mcDDI$sp(double arg0, int arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public double apply$mcDDJ$sp(double arg0, long arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public double apply$mcDID$sp(int arg0, double arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public double apply$mcDII$sp(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public double apply$mcDIJ$sp(int arg0, long arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public double apply$mcDJD$sp(long arg0, double arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public double apply$mcDJI$sp(long arg0, int arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public double apply$mcDJJ$sp(long arg0, long arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public float apply$mcFDD$sp(double arg0, double arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public float apply$mcFDI$sp(double arg0, int arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public float apply$mcFDJ$sp(double arg0, long arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public float apply$mcFID$sp(int arg0, double arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public float apply$mcFII$sp(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public float apply$mcFIJ$sp(int arg0, long arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public float apply$mcFJD$sp(long arg0, double arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public float apply$mcFJI$sp(long arg0, int arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public float apply$mcFJJ$sp(long arg0, long arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int apply$mcIDD$sp(double arg0, double arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int apply$mcIDI$sp(double arg0, int arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int apply$mcIDJ$sp(double arg0, long arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int apply$mcIID$sp(int arg0, double arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int apply$mcIII$sp(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int apply$mcIIJ$sp(int arg0, long arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int apply$mcIJD$sp(long arg0, double arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int apply$mcIJI$sp(long arg0, int arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int apply$mcIJJ$sp(long arg0, long arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long apply$mcJDD$sp(double arg0, double arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long apply$mcJDI$sp(double arg0, int arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long apply$mcJDJ$sp(double arg0, long arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long apply$mcJID$sp(int arg0, double arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long apply$mcJII$sp(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long apply$mcJIJ$sp(int arg0, long arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long apply$mcJJD$sp(long arg0, double arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long apply$mcJJI$sp(long arg0, int arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long apply$mcJJJ$sp(long arg0, long arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void apply$mcVDD$sp(double arg0, double arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void apply$mcVDI$sp(double arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void apply$mcVDJ$sp(double arg0, long arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void apply$mcVID$sp(int arg0, double arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void apply$mcVII$sp(int arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void apply$mcVIJ$sp(int arg0, long arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void apply$mcVJD$sp(long arg0, double arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void apply$mcVJI$sp(long arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void apply$mcVJJ$sp(long arg0, long arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean apply$mcZDD$sp(double arg0, double arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean apply$mcZDI$sp(double arg0, int arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean apply$mcZDJ$sp(double arg0, long arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean apply$mcZID$sp(int arg0, double arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean apply$mcZII$sp(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean apply$mcZIJ$sp(int arg0, long arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean apply$mcZJD$sp(long arg0, double arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean apply$mcZJI$sp(long arg0, int arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean apply$mcZJJ$sp(long arg0, long arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Function1<JsonFactory, Function1<String, com.fasterxml.jackson.core.JsonParser>> curried() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Function1<Tuple2<JsonFactory, String>, com.fasterxml.jackson.core.JsonParser> tupled() {
			// TODO Auto-generated method stub
			return null;
		}

		
	}
	
	public class ConvertUTF8 implements Function1<String,UTF8String> {

		@Override
		public <A> Function1<String, A> andThen(Function1<UTF8String, A> arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public UTF8String apply(String json) {
			return UTF8String.fromString(json);
		}

		@Override
		public double apply$mcDD$sp(double arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public double apply$mcDF$sp(float arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public double apply$mcDI$sp(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public double apply$mcDJ$sp(long arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public float apply$mcFD$sp(double arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public float apply$mcFF$sp(float arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public float apply$mcFI$sp(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public float apply$mcFJ$sp(long arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int apply$mcID$sp(double arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int apply$mcIF$sp(float arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int apply$mcII$sp(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int apply$mcIJ$sp(long arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long apply$mcJD$sp(double arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long apply$mcJF$sp(float arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long apply$mcJI$sp(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long apply$mcJJ$sp(long arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void apply$mcVD$sp(double arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void apply$mcVF$sp(float arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void apply$mcVI$sp(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void apply$mcVJ$sp(long arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean apply$mcZD$sp(double arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean apply$mcZF$sp(float arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean apply$mcZI$sp(int arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean apply$mcZJ$sp(long arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public <A> Function1<A, UTF8String> compose(Function1<A, String> arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
