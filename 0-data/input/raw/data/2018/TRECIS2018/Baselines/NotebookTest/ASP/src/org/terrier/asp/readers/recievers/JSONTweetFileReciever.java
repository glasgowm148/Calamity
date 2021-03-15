package org.terrier.asp.readers.recievers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.terrier.asp.conf.ASPReaderConf;
import org.terrier.asp.interfaces.StreamReader;
import org.terrier.asp.readers.utility.Tweet;

/**
 * This is a reciver that ingests a stream of JSON format tweets read from
 * a file on disk. 
 * 
 * Variables:
 *  - inputFile : The name of the input file to read tweets from
 * 
 * Output Frames Implement:
 *  - KeyFrame (tweet id)
 *  - TextFrame (tweet text)
 *  - MetaDataFrame (all tweet properties as JSON)
 * 
 * @author richardm
 *
 */
public class JSONTweetFileReciever extends StreamReader {

	private static final long serialVersionUID = 866772405823259959L;

	/** This is the underlying thread that is reading the input and
	 * adding the Rows to the buffer */
	protected RecieverThread internalThread = null;
	
	/** This is the input tweet file to get the data from */
	public String inputFile;
	
	/** The Schema for the rows produced by each Reciever */
	public StructType schema;
	
	/**
	 * Create a new JSONTweetFileReciever using a file on disk
	 * @param inputFile
	 */
	public JSONTweetFileReciever(ASPReaderConf conf) {
		super(conf);
		this.inputFile = conf.getVariables().get("inputFile");
		
		schema = new StructType(); 
		schema = schema.add("KeyFrame", DataTypes.StringType, false);
		schema = schema.add("TextFrame", DataTypes.StringType, false);
		schema = schema.add("MetaDataFrame", DataTypes.StringType, false);
		
	}

	@Override
	public void onStart() {
		if (internalThread!=null) internalThread.kill();
		internalThread = new RecieverThread(inputFile);
		Thread t = new Thread(internalThread);
		t.start();
		
	}

	@Override
	public void onStop() {
		if (internalThread!=null) internalThread.kill();
	}
	
	/**
	 * General method for adding a row to the reciever buffer
	 * @param row
	 */
	public void storeRow(Row row) {
		store(row);
	}
	
	/**
	 * This is a reciever thread that reads a JSON Tweet file, converts each
	 * tweet to a Row object and adds it to the buffer.
	 * @author richardm
	 *
	 */
	public class RecieverThread implements Runnable {

		/** The file to read tweets from */
		String gzipFile;
		
		/** Kill this thread? */
		boolean kill = false;
		
		/** Object Mapper */
		ObjectMapper mapper = new ObjectMapper();
		
		/**
		 * Create the reader from a file
		 * @param gzipFile
		 */
		public RecieverThread(String gzipFile) {
			this.gzipFile = gzipFile;
		}
		
		@Override
		public void run() {
			
			
			// check whether we should store meta data about each tweet
			boolean ignoreMetaData = false;
			if (conf.getVariables().containsKey("ignoreMetaData") && conf.getVariables().get("ignoreMetaData").equalsIgnoreCase("true")) ignoreMetaData=true;
			
			BufferedReader br;
			try {
				if (gzipFile.endsWith("gz")) br = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(gzipFile)),"UTF-8"));
				else br = new BufferedReader(new InputStreamReader(new FileInputStream(gzipFile),"UTF-8"));
				
				String line;
				while ((line = br.readLine())!=null) {
					if (kill) break;
					storeRow(Tweet.convertToRow(line, ignoreMetaData));
				}
				
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


		/**
		 * Is this thread queued to kill?
		 * @return
		 */
		public boolean killIsSet() {
			return kill;
		}

		/**
		 * Kill this thread
		 */
		public void kill() {
			this.kill = true;
		}
		
		
		
	}

	@Override
	public StructType getSchema() {
		return schema;
	}
	
	

}
