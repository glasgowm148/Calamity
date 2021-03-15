package org.terrier.asp.interfaces;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.receiver.Receiver;
import org.terrier.asp.conf.ASPReaderConf;

/**
 * This class represents a reader that produces a JavaDStream<Row>
 * that can be passed into the module pipeline.
 * @author richardm
 *
 */
public abstract class StreamReader extends Receiver<Row> {

	private static final long serialVersionUID = -3513819901746520574L;
	
	/** This is the configuration of the reader **/
	public ASPReaderConf conf;

	/**
	 * Default constructor for a stream reader that uses a 
	 * pre-loaded ASPReaderConf file.
	 * @param conf
	 */
	public StreamReader(ASPReaderConf conf) {
		super(StorageLevel.MEMORY_AND_DISK_2());
		this.conf = conf;
	}
	
	/**
	 * Get the schema of the rows in the stream produced by
	 * the Reciever
	 */
	public abstract StructType getSchema();
	
}
