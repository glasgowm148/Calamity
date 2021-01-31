package org.terrier.asp.readers.datasets;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.terrier.asp.applications.ASPProgramScheduler;
import org.terrier.asp.conf.ASPReaderConf;
import org.terrier.asp.interfaces.DatasetReader;
import org.terrier.asp.readers.utility.Tweet;

/**
 * This is a generic reader that produces a dataset out of a
 * gzipped Twitter JSON file. It reads the entire dataset into
 * memory.
 * @author richardm
 *
 */
public class TwitterJSONDatasetInMem extends DatasetReader {

	/** This is the input tweet file to get the data from */
	public String inputFile;
	
	/** The Schema for the rows produced by each Reciever */
	public StructType schema;
	
	// Logger
	static Logger logger;
	
	public TwitterJSONDatasetInMem(ASPReaderConf conf, SparkSession spark) {
		super(conf, spark);
		
		schema = new StructType(); 
		schema = schema.add("KeyFrame", DataTypes.StringType, false);
		schema = schema.add("TextFrame", DataTypes.StringType, false);
		schema = schema.add("MetaDataFrame", DataTypes.StringType, false);
		
		logger = LogManager.getLogger(TwitterJSONDatasetInMem.class);
	}

	@Override
	public Dataset<Row> getDataset() {
		
		logger.info("      Loading Dataset into Memory on Host...");
		
		List<Row> datasetInMemory = new ArrayList<Row>();
		
		String gzipFile = conf.getVariables().get("inputFile");
		
		logger.info("      Processing '"+gzipFile+"'");
		
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
		
		BufferedReader br;
		String line = null;
		try {
			if (gzipFile.endsWith("gz")) br = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(gzipFile)),"UTF-8"));
			else br = new BufferedReader(new InputStreamReader(new FileInputStream(gzipFile),"UTF-8"));
			
			while ((line = br.readLine())!=null) {
				
				if (countSinceLastReport==0) {
					logger.info("        Loaded "+recordCount+" records so far");
					
					countSinceLastReport = onloadLogEveryNRows;
				}
				try {
					datasetInMemory.add(Tweet.convertToRow(line, ignoreMetaData));
				} catch (Exception e) {
					logger.warn("Parsing Failed for '"+line+"'");
					continue;
				}
				recordCount++;
				countSinceLastReport--;
				if (loadOnlyFirstNRows>0 && recordCount>=loadOnlyFirstNRows) break;
			}
			
			br.close();
		} catch (Exception e) {
			System.err.println(line);
			e.printStackTrace();
		}
		
		return spark.createDataFrame(datasetInMemory, schema);
	}

	@Override
	public StructType getSchema() {
		return schema;
	}

}
