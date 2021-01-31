package org.terrier.asp.modules.stream;

import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.terrier.asp.conf.ASPModuleConf;
import org.terrier.asp.interfaces.StreamingModule;


public class CreateOneVsAllModule extends StreamingModule implements Function<Row,Row>,MapFunction<Row,Row> {

	private static final long serialVersionUID = 4545645884674868699L;
	int fieldIndexCategoryFrame = -1;
	double otherCategory = 0.0;
	double targetCategory = 1.0;
	
	/** Constructor */
	public CreateOneVsAllModule(ASPModuleConf configuration) {
		super(configuration);
	}
	

	@Override
	public Row call(Row row) throws Exception {
		if (fieldIndexCategoryFrame==-1)
			fieldIndexCategoryFrame = inputSchema.fieldIndex("label");
		
		double category = (double) row.get(fieldIndexCategoryFrame);
		
		if (!conf.getVariables().get("category").equalsIgnoreCase(String.valueOf(category))) {
			Object[] newRow = new Object[row.length()];
			for (int i =0; i<row.length(); i++) newRow[i] = row.get(i);
			newRow[fieldIndexCategoryFrame] = otherCategory;
			return RowFactory.create(newRow);
		} else {
			Object[] newRow = new Object[row.length()];
			for (int i =0; i<row.length(); i++) newRow[i] = row.get(i);
			newRow[fieldIndexCategoryFrame] = targetCategory;
			return RowFactory.create(newRow);
		}
	}

	@Override
	public JavaDStream<Row> execute(JavaDStream<Row> inputDataset) {
		return inputDataset.map(this);
	}
	
	public void setCategory(String category) {
		conf.getVariables().put("category", category);
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


	@Override
	public String explain() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
