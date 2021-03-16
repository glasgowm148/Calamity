package org.terrier.asp.modules.batch;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.bson.Document;
import org.terrier.asp.conf.ASPModuleConf;
import org.terrier.asp.interfaces.BatchModule;
import org.terrier.asp.readers.utility.SchemaAsJson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * This is an ASP writer module that instead of writing the system output to a file it instead
 * writes the dataset to a table in a MongoDB database.
 * @author richardm
 *
 */
public class MongoDBWriterModule extends BatchModule{

	/** Module logger */
	protected Logger logger;
	
	
	public MongoDBWriterModule(ASPModuleConf configuration) {
		super(configuration);
		
		logger = LogManager.getLogger(MongoDBWriterModule.class);
	}
	
	@Override
	public Dataset<Row> execute(Dataset<Row> inputDataset) {
	
		
		Dataset<String> jsonDataset = inputDataset.toJSON();
		
		String host = conf.getVariables().get("host");
		String databaseName = conf.getVariables().get("databaseName");
		String collectionName = conf.getVariables().get("collectionName");
		boolean ovewriteRows = Boolean.parseBoolean(conf.getVariables().get("ovewriteRows")); 
		
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
				
		try {
			List<String> collectedLocally = jsonDataset.collectAsList();
			
			
			
			for (String row : collectedLocally) {
				
				Document bson = Document.parse(row);
				String keyframe = bson.getString("KeyFrame");

				try {
					Document existingDoc = collection.find(eq("KeyFrame", keyframe)).first();
					if (existingDoc==null) {
						collection.insertOne(bson);
					} else {
						if (ovewriteRows) collection.findOneAndReplace(existingDoc, bson);
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println(row);
				}
				
			}
			
			
			// now write the special row that contains the schema
			
			StructType storeSchema = getOutputSchema();
			List<String> schemaFieldNames = new ArrayList<String>(storeSchema.fieldNames().length);
			for (String fieldName : storeSchema.fieldNames()) schemaFieldNames.add(fieldName);
			
			List<String> schemaFieldDataTypeJson = new ArrayList<String>(storeSchema.fieldNames().length);
			for (StructField field : storeSchema.fields()) schemaFieldDataTypeJson.add(field.dataType().json());
			
			SchemaAsJson jsonSchema = new SchemaAsJson(schemaFieldNames, schemaFieldDataTypeJson);
			
			ObjectWriter oWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
			String outputSchemaAsString = oWriter.writeValueAsString(jsonSchema);
			System.err.println(outputSchemaAsString);
			JsonParser parser = new JsonParser();
			JsonObject editedScheama = parser.parse(outputSchemaAsString).getAsJsonObject();
			editedScheama.addProperty("KeyFrame", "SCHEMA");
			Document bson = Document.parse(editedScheama.toString());

			Document existingDoc = collection.find(eq("KeyFrame", "SCHEMA")).first();
			if (existingDoc==null) {
				collection.insertOne(bson);
			} else {
				if (ovewriteRows) collection.findOneAndReplace(existingDoc, bson);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mongoClient.close();
		
		return inputDataset;
	}

	@Override
	public StructType getOutputSchema() {
		return inputSchema;
	}

	@Override
	public String explain() {
		// TODO Auto-generated method stub
		return null;
	}

}
