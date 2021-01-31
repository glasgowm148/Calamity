package org.terrier.asp.interfaces;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.StructType;
import org.terrier.asp.conf.ASPModuleConf;

/**
 * This is the interface for a basic batch module in ASP. A Batch
 * Module processes a Spark Dataset in a batch operation. In practice,
 * Batch Modules are normally run prior to kick-off of the main stream
 * processing.
 * @author richardm
 *
 */
public abstract class BatchModule implements ASPModule {

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
	public BatchModule(ASPModuleConf configuration) {
		this.conf = configuration;
	}
	
	/**
	 * This is the main processing method for the Batch Module. Calling
	 * execute will queue processing of this module on the provided
	 * dataset using the specified mode
	 * @param mode - which mode should be used
	 * @param inputDataset - what data should be processed
	 * @return
	 */
	public abstract Dataset<Row> execute(Dataset<Row> inputDataset);
	
	
	/**
	 * Get the schema of the rows in the dataset produced by
	 * this Module
	 */
	public abstract StructType getOutputSchema();
	
	/** Set the schema for the input dataset, allows us to lookup frames by name */
	public boolean setInputSchema(StructType schema) {
		this.inputSchema = schema;
		
		return true;
	}
	
}
