package org.terrier.asp.readers.utility;

import java.util.List;

public class SchemaAsJson {

	List<String> fieldNames;
	List<String> fieldDataTypeJson;
	
	public SchemaAsJson() {}

	public SchemaAsJson(List<String> fieldNames, List<String> fieldDataTypeJson) {
		super();
		this.fieldNames = fieldNames;
		this.fieldDataTypeJson = fieldDataTypeJson;
	}

	public List<String> getFieldNames() {
		return fieldNames;
	}

	public void setFieldNames(List<String> fieldNames) {
		this.fieldNames = fieldNames;
	}

	public List<String> getFieldDataTypeJson() {
		return fieldDataTypeJson;
	}

	public void setFieldDataTypeJson(List<String> fieldDataTypeJson) {
		this.fieldDataTypeJson = fieldDataTypeJson;
	}
	

	
	
	
}
