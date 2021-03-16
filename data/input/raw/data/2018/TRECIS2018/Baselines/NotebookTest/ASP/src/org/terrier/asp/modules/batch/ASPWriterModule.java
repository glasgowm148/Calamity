package org.terrier.asp.modules.batch;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.StructType;
import org.terrier.asp.conf.ASPModuleConf;
import org.terrier.asp.interfaces.BatchModule;

public class ASPWriterModule extends BatchModule{

	String datasetID = "UnnamedDataset";
	
	public ASPWriterModule(ASPModuleConf configuration) {
		super(configuration);
	}
	
	public void setDatasetID(String datasetID) {
		this.datasetID = datasetID;
	}
	
	@Override
	public Dataset<Row> execute(Dataset<Row> inputDataset) {
		
		Dataset<String> jsonDataset = inputDataset.toJSON();
		
		String outputDIR = conf.getVariables().get("outputDIR");		
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(outputDIR+"/"+datasetID+".json.gz"))));
			List<String> collectedLocally = jsonDataset.collectAsList();
			
			
			
			for (String row : collectedLocally) {
				bw.write(row.replace("\n", ""));
				bw.write("\n");
			}

			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return inputDataset;
	}

	@Override
	public StructType getOutputSchema() {
		return inputSchema;
	}

	@Override
	public String explain() {
		// TODO Auto-generated method stub
		return null;
	}

}
