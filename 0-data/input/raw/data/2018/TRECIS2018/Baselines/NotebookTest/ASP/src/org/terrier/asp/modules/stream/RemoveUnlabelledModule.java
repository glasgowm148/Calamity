package org.terrier.asp.modules.stream;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.terrier.asp.conf.ASPModuleConf;
import org.terrier.asp.interfaces.StreamingModule;

public class RemoveUnlabelledModule extends StreamingModule implements FlatMapFunction<Row,Row>{

	private static final long serialVersionUID = -6152004255841771544L;
	int fieldIndexCategoryFrame = -1;
	
	public RemoveUnlabelledModule(ASPModuleConf configuration) {
		super(configuration);
	}
	
	public RemoveUnlabelledModule(ASPModuleConf configuration, int fieldIndexCategoryFrame) {
		super(configuration);
		this.fieldIndexCategoryFrame = fieldIndexCategoryFrame;
	}
	
	@Override
	public Iterator<Row> call(Row row) throws Exception {
		if (fieldIndexCategoryFrame==-1)
			fieldIndexCategoryFrame = inputSchema.fieldIndex("AssessedCategory");
		
		String category = (String) row.get(fieldIndexCategoryFrame);
		
		if (category.length()==0) {
			List<Row> filtered = new ArrayList<Row>(0);
			return filtered.iterator();
		} else {
			List<Row> filtered = new ArrayList<Row>(1);
			filtered.add(row);
			return filtered.iterator();
		}

	}

	@Override
	public JavaDStream<Row> execute(JavaDStream<Row> inputDataset) {
		return inputDataset.flatMap(this);
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
		return "This module removes any tweets from the dataset that do not have a valid 'AssessedCategory' field.";
	}

	
}
