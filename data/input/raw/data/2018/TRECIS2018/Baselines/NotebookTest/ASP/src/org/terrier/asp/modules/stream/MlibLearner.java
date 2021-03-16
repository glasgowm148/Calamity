package org.terrier.asp.modules.stream;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.terrier.asp.conf.ASPModuleConf;
import org.terrier.asp.interfaces.StreamingModule;

public class MlibLearner extends StreamingModule{

	public MlibLearner(ASPModuleConf configuration) {
		super(configuration);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JavaDStream<Row> execute(JavaDStream<Row> inputDataset) {

		
		return null;
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
