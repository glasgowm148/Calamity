package org.terrier.asp.interfaces;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.terrier.asp.conf.ASPModuleConf;

/**
 * This interface identifies a java class as being a Module
 * implementation. 
 * @author richardm
 *
 */
public abstract class StreamingModule implements ASPModule {

	/** The configuration information for this module */
	protected ASPModuleConf conf;
	
	/** The input schema type */
	protected StructType inputSchema;
	
	/** 
	 * Standard constructor for a module, takes as input a
	 * ASPModuleConf object that has been loaded from a file
	 * and the mode for the module.
	 * @param configuration
	 */
	public StreamingModule(ASPModuleConf configuration) {
		this.conf = configuration;
	}
	
	public StreamingModule() {}
	
	/**
	 * This is the main processing method for the Module. Calling
	 * execute will queue processing of this module on the provided
	 * dataset using the specified mode
	 * @param mode - which mode should be used
	 * @param inputDataset - what data should be processed
	 * @return
	 */
	public abstract JavaDStream<Row> execute(JavaDStream<Row> inputDataset);
	
	/** Set the schema for the input dataset, allows us to lookup frames by name */
	public boolean setInputSchema(StructType schema) {
		this.inputSchema = schema;
		
		
		return true;
	}
	
}
