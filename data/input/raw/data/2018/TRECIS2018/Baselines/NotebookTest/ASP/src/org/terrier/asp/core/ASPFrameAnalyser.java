package org.terrier.asp.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

/**
 * This us a utility class that provides methods to get information
 * about a particular data frame/dataset. This is the main way to 
 * check what standard frame formats a data frame implements. The
 * ASP template manager uses this information to check that the
 * inputs and outputs of the different modules are valid.
 * @author richardm
 *
 */
public class ASPFrameAnalyser {
	
	public static List<String> implementedFormats(Dataset<Row> dataset) {
		
		List<String> formats = new ArrayList<String>();

		for (String field : dataset.schema().fieldNames()) formats.add(field);
		
		return formats;
		
	}

}
