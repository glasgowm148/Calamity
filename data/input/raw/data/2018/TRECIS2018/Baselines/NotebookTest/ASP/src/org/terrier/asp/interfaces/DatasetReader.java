package org.terrier.asp.interfaces;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;
import org.terrier.asp.conf.ASPReaderConf;

/**
 * This is an abstract class that represents a reader of a dataset.
 * It returns a Spark Dataset<Row> object that can then be processed
 * in a batch operation. 
 * @author richardm
 *
 */
public abstract class DatasetReader {

	/** This is the configuration of the reader **/
	protected ASPReaderConf conf;
	
	/** This is the current spark session, it can be used to read files */
	protected SparkSession spark;
	
	/**
	 * Default constructor for a stream reader that uses a 
	 * pre-loaded ASPReaderConf file.
	 * @param conf
	 */
	public DatasetReader(ASPReaderConf conf, SparkSession spark) {
		this.conf = conf;
		this.spark = spark;
	}
	
	/**
	 * Returns the dataset associated to this reader as a
	 * Spark Dataset<Row> object. 
	 * @return
	 */
	public abstract Dataset<Row> getDataset();
	
	/**
	 * Get the schema of the rows in the dataset contained in
	 * the Dataset
	 */
	public abstract StructType getSchema();
	
}
