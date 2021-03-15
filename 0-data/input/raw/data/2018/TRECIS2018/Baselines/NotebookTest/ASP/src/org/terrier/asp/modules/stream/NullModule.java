package org.terrier.asp.modules.stream;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.terrier.asp.conf.ASPModuleConf;
import org.terrier.asp.interfaces.StreamingModule;

/**
 * This is a module that does nothing. It is used for testing
 * only.
 * @author richardm
 *
 */
public class NullModule extends StreamingModule {
	
	public NullModule(ASPModuleConf configuration) {
		super(configuration); 
	}

	@Override
	public JavaDStream<Row> execute(JavaDStream<Row> inputDataset) {
		
		return inputDataset;
	}

	@Override
	public StructType getOutputSchema() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String explain() {
		// TODO Auto-generated method stub
		return null;
	}



}
