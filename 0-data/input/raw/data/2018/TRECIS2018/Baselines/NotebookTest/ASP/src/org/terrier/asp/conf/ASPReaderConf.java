package org.terrier.asp.conf;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ASPReaderConf implements Serializable{

	private static final long serialVersionUID = 3708568426580426286L;

	/** The unique identifier for the module instance **/
	String readerID;
	
	/** The class to use to instantiate the module **/
	String fullClassName;
	
	/** Any variables that the module needs **/
	Map<String,String> variables;
	
	/** This is a list of standard frame formats that the output 
	 * frames will have. There may be others.  */
	List<String> guaranteedOutputFrameTypes;

	/** Logger */
	static Logger logger = LogManager.getLogger(ASPReaderConf.class);
	
	/**
	 * Default Constructor, will not be used in most situations
	 * @param moduleID
	 * @param fullClassName
	 * @param variables
	 * @param requiredModule2Dependancy
	 * @param moduleDepandancyFiles
	 * @param requiredInputFrameTypes
	 * @param requiredOutputFrameTypes
	 */
	public ASPReaderConf(String readerID, 
			String fullClassName,
			Map<String, String> variables,
			List<String> guaranteedOutputFrameTypes) {
		super();
		this.readerID = readerID;
		this.fullClassName = fullClassName;
		this.variables = variables;
		this.guaranteedOutputFrameTypes = guaranteedOutputFrameTypes;
	}

	/** Constructor for automatic creation */
	public ASPReaderConf() {}
	
	/**
	 * Load this modules configuration from a JSON text file.
	 * @param confFile
	 */
	public ASPReaderConf(String confFile) {
		
		try {
			load(confFile);
		} catch (Exception e) {
			logger.log(Level.ERROR, "   ASP Reader Configuration load [failed]");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Internal load method for an ASP Module configuration file.
	 * @param confFile
	 * @throws Exception 
	 */
	public void load(String confFile) throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();
		
		ASPReaderConf confTMP = mapper.readValue(new File(confFile), ASPReaderConf.class); 
		this.readerID = confTMP.readerID;
		this.fullClassName = confTMP.fullClassName;
		this.variables = confTMP.getVariables();
		this.guaranteedOutputFrameTypes = confTMP.guaranteedOutputFrameTypes;
		
	}
	
	/**
	 * Internal save method allowing for the contents of this
	 * configuration to be written to disk.
	 * @param confFile
	 * @throws Exception 
	 */
	public void save(String confFile) throws Exception {
		
		
		ObjectMapper mapper = new ObjectMapper();

		//Object to JSON in file
		mapper.writerWithDefaultPrettyPrinter().writeValue(new File(confFile), this);
		
	}

	public String getReaderID() {
		return readerID;
	}

	public void setReaderID(String moduleID) {
		this.readerID = moduleID;
	}

	public String getFullClassName() {
		return fullClassName;
	}

	public void setFullClassName(String fullClassName) {
		this.fullClassName = fullClassName;
	}

	public Map<String, String> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, String> variables) {
		this.variables = variables;
	}
	
	public List<String> getGuaranteedOutputFrameTypes() {
		return guaranteedOutputFrameTypes;
	}

	public void setGuaranteedOutputFrameTypes(
			List<String> guaranteedOutputFrameTypes) {
		this.guaranteedOutputFrameTypes = guaranteedOutputFrameTypes;
	}
}
