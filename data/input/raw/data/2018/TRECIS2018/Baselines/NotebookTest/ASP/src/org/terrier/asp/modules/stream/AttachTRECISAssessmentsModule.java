package org.terrier.asp.modules.stream;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.terrier.asp.conf.ASPModuleConf;
import org.terrier.asp.interfaces.StreamingModule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * This class attaches potential categories for the text item based on indicator terms it contains.
 * Indicator terms are provided in the form of a JSON file output by the TRECIS assessment interface.
 * @author richardm
 *
 */
public class AttachTRECISAssessmentsModule extends StreamingModule implements Function<Row,Row>,MapFunction<Row,Row> {

	private static final long serialVersionUID = 5361084786654217188L;

	/** This contains the mapping for a single term to a category label */
	public Map<String,String> oneTermToOneCategory;
	
	/** This contains the mapping for a single term to multiple possible category labels */
	public Map<String,Set<String>> oneTermToMultipleCategories;
	
	public Map<String,String> itemID2Category;
	
	int fieldIndexKeyFrame = -1;
	int fieldIndexTextFrame = -1;
	
	ObjectMapper mapper;
	
	
	/** Constructor */
	public AttachTRECISAssessmentsModule(ASPModuleConf configuration) {
		super(configuration);
		loadIndicatorTermsToCategoriesMapping();
		mapper = new ObjectMapper();
	}
	
	public AttachTRECISAssessmentsModule(ASPModuleConf configuration, int fieldIndexKeyFrame, int fieldIndexTextFrame) {
		super(configuration);
		loadIndicatorTermsToCategoriesMapping();
		mapper = new ObjectMapper();
		this.fieldIndexKeyFrame = fieldIndexKeyFrame;
		this.fieldIndexTextFrame = fieldIndexTextFrame;
		
	}
	
	public AttachTRECISAssessmentsModule() {
		super();
	}
	
	public void loadIndicatorTermsToCategoriesMapping() {
		
		oneTermToOneCategory = new HashMap<String,String>();
		oneTermToMultipleCategories = new HashMap<String,Set<String>>();
		itemID2Category = new HashMap<String,String>();
		
		JsonParser parser = new JsonParser();
		try {
			JsonObject assessmentDumpFile = parser.parse(new FileReader(conf.getVariables().get("assessmentDumpFile"))).getAsJsonObject();
			
			JsonArray events = assessmentDumpFile.get("events").getAsJsonArray();
			Iterator<JsonElement> eventsI = events.iterator();
			while (eventsI.hasNext()) {
				JsonObject event = eventsI.next().getAsJsonObject();
				JsonArray tweets = event.get("tweets").getAsJsonArray();
				Iterator<JsonElement> tweetsI = tweets.iterator();
				while (tweetsI.hasNext()) {
					JsonObject assessment = tweetsI.next().getAsJsonObject();
					String postID = assessment.get("postID").getAsString();
					String category = assessment.get("categories").getAsJsonArray().get(0).getAsString();
					itemID2Category.put(postID, category);
					
					JsonArray indicatorTerms = assessment.get("indicatorTerms").getAsJsonArray();
					Iterator<JsonElement> termsI = indicatorTerms.iterator();
					while (termsI.hasNext()) {
						String indicatorTerm = termsI.next().getAsString().toLowerCase().trim();
						if (oneTermToMultipleCategories.containsKey(indicatorTerm)) oneTermToMultipleCategories.get(indicatorTerm).add(category);
						else {
							if (oneTermToOneCategory.containsKey(indicatorTerm)) {
								if (oneTermToOneCategory.get(indicatorTerm).equalsIgnoreCase(category)) continue;
								else {
									Set<String> multiCategory = new HashSet<String>();
									multiCategory.add(indicatorTerm);
									multiCategory.add(oneTermToOneCategory.remove(indicatorTerm));
									oneTermToMultipleCategories.put(indicatorTerm, multiCategory);
								}
							} else {
								oneTermToOneCategory.put(indicatorTerm, category);
							}
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public JavaDStream<Row> execute(JavaDStream<Row> inputDataset) {
		return inputDataset.map(this);
	}


	@Override
	public Row call(Row row) throws Exception {
		
		// store the positional information for the text frame so we don't need to look it up for every row
		if (fieldIndexTextFrame==-1)
			fieldIndexTextFrame = inputSchema.fieldIndex("TextFrame");
		
		if (fieldIndexKeyFrame==-1)
			fieldIndexKeyFrame = inputSchema.fieldIndex("KeyFrame");
		
		// text of the item
		String text = ((String) row.get(fieldIndexTextFrame)).toLowerCase().trim();
		
		List<String> categoriesMatched = null;
		
		// we are going to do a simple whitespace term match here
		for (String term : text.split(" ")) {
			if (oneTermToOneCategory.containsKey(term)) {
				if (categoriesMatched==null) categoriesMatched = new ArrayList<String>(1);
				categoriesMatched.add(oneTermToOneCategory.get(term));
			} else if (oneTermToMultipleCategories.containsKey(term)) {
				if (categoriesMatched==null) categoriesMatched = new ArrayList<String>(oneTermToMultipleCategories.get(term).size());
				for (String category : oneTermToMultipleCategories.get(term)) categoriesMatched.add(category);
			}
		}
		
		Object[] newRow = new Object[row.length()+2];
		for (int i =0; i<row.length(); i++) newRow[i] = row.get(i);
		
		if (categoriesMatched!=null) newRow[newRow.length-2] = mapper.writeValueAsString(categoriesMatched);
		else newRow[newRow.length-2] = "[]";
		
		String postID = (String) row.get(fieldIndexKeyFrame);
		
		if (itemID2Category.containsKey(postID)) newRow[newRow.length-1] = itemID2Category.get(postID);
		else newRow[newRow.length-1] = "";
		
		return RowFactory.create(newRow);
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

	@Override
	public String explain() {
		// TODO Auto-generated method stub
		return null;
	}

}
