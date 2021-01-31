package org.terrier.asp.interfaces;

import org.apache.spark.sql.types.StructType;

/**
 * This is the top-level interface for a Module in ASP. It represents the
 * methods that all modules must implement.
 * @author richardm
 *
 */
public interface ASPModule {

	
	/** Get the schema for rows output by this module */
	public StructType getOutputSchema();
	
	/** Set the schema for the input dataset and checks that it is valid */
	public boolean setInputSchema(StructType schema);
	
	/** Returns an explanation of what this module does */
	public String explain();
	
}
