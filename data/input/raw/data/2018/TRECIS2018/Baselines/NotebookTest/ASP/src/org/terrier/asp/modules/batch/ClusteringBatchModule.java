package org.terrier.asp.modules.batch;

import org.apache.spark.ml.clustering.KMeans;
import org.apache.spark.ml.clustering.KMeansModel;
import org.apache.spark.ml.feature.HashingTF;
import org.apache.spark.ml.feature.IDF;
import org.apache.spark.ml.feature.IDFModel;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.terrier.asp.conf.ASPModuleConf;
import org.terrier.asp.interfaces.BatchModule;

public class ClusteringBatchModule extends BatchModule{

	int indexOfTextFrame = -1;
	
	public ClusteringBatchModule(ASPModuleConf configuration,
			String moduleMode) {
		super(configuration);
	}

	@Override
	public Dataset<Row> execute(Dataset<Row> inputDataset) {

		if (indexOfTextFrame==-1) indexOfTextFrame = inputSchema.fieldIndex("TextFrame");

		Tokenizer tokenizer = new Tokenizer().setInputCol("TextFrame").setOutputCol("WordsFrame");
		Dataset<Row> wordsData = tokenizer.transform(inputDataset);
		
		int numFeatures = 5000;
		if (conf.getVariables().containsKey("numOFWordsToUseAsFeatures")) numFeatures = Integer.parseInt(conf.getVariables().get("numOFWordsToUseAsFeatures"));
		HashingTF hashingTF = new HashingTF()
		  .setInputCol("WordsFrame")
		  .setOutputCol("TFFrame")
		  .setNumFeatures(numFeatures);

		Dataset<Row> featurizedData = hashingTF.transform(wordsData);
		// alternatively, CountVectorizer can also be used to get term frequency vectors

		featurizedData = featurizedData.drop(featurizedData.col("WordsFrame"));
		
		IDF idf = new IDF().setInputCol("TFFrame").setOutputCol("IDFFrame");
		IDFModel idfModel = idf.fit(featurizedData);

		Dataset<Row> rescaledData = idfModel.transform(featurizedData);
		
		rescaledData = rescaledData.drop(rescaledData.col("TFFrame"));
		
		int k = 10;
		if (conf.getVariables().containsKey("numClusters")) k = Integer.parseInt(conf.getVariables().get("numClusters"));
		
		KMeans kmeans = new KMeans()
				.setK(k)
				.setSeed(1L)
				.setFeaturesCol("IDFFrame")
				.setPredictionCol("KMeansClusterFrame");
		KMeansModel model = kmeans.fit(rescaledData);

		// Make predictions
		Dataset<Row> predictions = model.transform(rescaledData);
		
		predictions = predictions.drop(predictions.col("IDFFrame"));
		
		return predictions;
	}

	@Override
	public StructType getOutputSchema() {
		StructType outputSchema = new StructType();
		String[] fieldNames = inputSchema.fieldNames();
		for (int i =0; i< fieldNames.length; i++) {
			outputSchema = outputSchema.add(fieldNames[i], inputSchema.fields()[i].dataType(), false);
		}
		
		outputSchema.add("KMeansClusterFrame", DataTypes.createArrayType(DataTypes.IntegerType));
		
		return outputSchema;
	}

	@Override
	public String explain() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
