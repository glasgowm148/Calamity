package org.terrier.asp.readers.utility;

import java.io.IOException;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

public class Tweet {

	/** Object Mapper */
	public static ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * Convert a tweet to a Row object
	 * @param line
	 * @return
	 * @throws IOException 
	 * @throws JsonProcessingException 
	 */
	public static Row convertToRow(String line, boolean ignoreMetaData) throws Exception {
		
		Object[] frameComponents = new Object[3];
		
		// String to JSON Converter
	    JsonNode actualObj = mapper.readTree(line);
		
		// KeyFrame
	    String id = actualObj.get("id_str").getTextValue();
	    frameComponents[0] = id;
		
		// TextFrame
	    String text = normaliseString(actualObj.get("text").getTextValue());
	    /*ObjectNode textFrame = mapper.createObjectNode();
	    textFrame.put("TweetText", text);
	    frameComponents[1] = mapper.writeValueAsString(textFrame);*/
	    frameComponents[1] = text;
		
		// MetaDataFrame
	    if (ignoreMetaData) frameComponents[2] = "{}";
	    else frameComponents[2] = mapper.writeValueAsString(actualObj);
	    
		return RowFactory.create(frameComponents);
	}
	
	/**
	 * Convert a tweet to a Row object
	 * @param line
	 * @return
	 * @throws IOException 
	 * @throws JsonProcessingException 
	 */
	public static Row convertToRow(String line, boolean ignoreMetaData, String datasetID) throws Exception {
		
		Object[] frameComponents = new Object[4];
		
		// String to JSON Converter
	    JsonNode actualObj = mapper.readTree(line);
		
		// KeyFrame
	    String id = actualObj.get("id_str").getTextValue();
	    frameComponents[0] = id;
		
		// TextFrame
	    String text = normaliseString(actualObj.get("text").getTextValue());
	    /*ObjectNode textFrame = mapper.createObjectNode();
	    textFrame.put("TweetText", text);
	    frameComponents[1] = mapper.writeValueAsString(textFrame);*/
	    frameComponents[1] = text;
		
		// MetaDataFrame
	    if (ignoreMetaData) frameComponents[2] = "{}";
	    else frameComponents[2] = mapper.writeValueAsString(actualObj);
	    
	    frameComponents[3] = datasetID;
	    
		return RowFactory.create(frameComponents);
	}
	
	public static String normaliseString(String text) {
		//System.err.println(normaliseString("IN: "+text));
		String normaltext = text.toLowerCase();
		normaltext = normaltext.replace(".", " ");
		normaltext = normaltext.replace("\"", "'");
		normaltext = normaltext.replace("$", "DollarSymbol");
		normaltext = normaltext.replace("  ", " ");
		normaltext = normaltext.trim();
		return normaltext;
	}
	
}
