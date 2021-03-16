package org.terrier.asp.modules.batch;


import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.encoders.RowEncoder;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.terrier.asp.conf.ASPModuleConf;
import org.terrier.asp.interfaces.BatchModule;
import org.terrier.asp.modules.stream.AttachTRECISAssessmentsModule;


public class AttachTRECISLabels extends BatchModule{

	
	
	public AttachTRECISLabels(ASPModuleConf configuration) {
		super(configuration);
		
		
	}
	@Override
	public String explain() {
		return "This module takes a tweet dataset and attaches columns corresponding to human annotations for the TREC-IS high-level information categories. "
				+ "'LikelyCategories' indicate that the tweet contains one or more indicator terms for the named classes. "
				+ "'AssessedCategory' indiacates the best fit category as selected by a human annotator.";
	}

	@Override
	public Dataset<Row> execute(Dataset<Row> inputDataset) {

		// store the positional information for the text frame so we don't need to look it up for every row
		int indexOfTextFrame = inputSchema.fieldIndex("TextFrame");
				
		int indexOfKeyFrame = inputSchema.fieldIndex("KeyFrame");
		
		AttachTRECISAssessmentsModule streamModule = new AttachTRECISAssessmentsModule(conf,indexOfKeyFrame, indexOfTextFrame);
		streamModule.setInputSchema(inputSchema);
		
		return inputDataset.map(streamModule, RowEncoder.apply(streamModule.getOutputSchema()));
	}

	@Override
	public StructType getOutputSchema() {
		StructType outputSchema = new StructType(); 
		String[] fieldNames = this.inputSchema.fieldNames();
		for (int i=0; i<fieldNames.length; i++) {
			outputSchema = outputSchema.add(fieldNames[i], this.inputSchema.fields()[i].dataType(), false);
		}
		outputSchema = outputSchema.add("LikelyCategories", DataTypes.StringType, false);
		outputSchema = outputSchema.add("AssessedCategory", DataTypes.StringType, false);
		
		return outputSchema;
	}
	
	
	
	
	

}
