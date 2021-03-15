package org.terrier.asp.modules.batch;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.encoders.RowEncoder;
import org.apache.spark.sql.types.StructType;
import org.terrier.asp.conf.ASPModuleConf;
import org.terrier.asp.interfaces.BatchModule;
import org.terrier.asp.modules.stream.RemoveUnlabelledModule;

public class RemoveUnlabelled extends BatchModule{

	public RemoveUnlabelled(ASPModuleConf configuration) {
		super(configuration);
	}
	
	@Override
	public String explain() {
		return null;
	}

	@Override
	public Dataset<Row> execute(Dataset<Row> inputDataset) {

		// store the positional information for the text frame so we don't need to look it up for every row
		int indexOfAssessedCategory = inputSchema.fieldIndex("AssessedCategory");
		
		RemoveUnlabelledModule streamModule = new RemoveUnlabelledModule(conf,indexOfAssessedCategory);
		streamModule.setInputSchema(inputSchema);
		
		return inputDataset.flatMap(streamModule, RowEncoder.apply(streamModule.getOutputSchema()));
	}

	@Override
	public StructType getOutputSchema() {
		StructType outputSchema = new StructType(); 
		String[] fieldNames = inputSchema.fieldNames();
		for (int i=0; i<fieldNames.length; i++) {
			outputSchema = outputSchema.add(fieldNames[i], inputSchema.fields()[i].dataType(), false);
		}
		return outputSchema;
	}
}
