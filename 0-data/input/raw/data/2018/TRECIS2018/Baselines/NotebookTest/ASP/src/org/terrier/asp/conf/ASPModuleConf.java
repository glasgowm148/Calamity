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

/**
 * This class represents the configuration of an ASP module. It specifies
 * the module ID, any variables that the module needs, the class that
 * represents the module (i.e. what class to instantiate), as well as the 
 * IO information about that module (what it consumes and
 * produces). 
 * 
 * It can optionally specify any other modules that it depends on.
 * @author richardm
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class ASPModuleConf implements Serializable{

	private static final long serialVersionUID = -4215809610740960432L;

	/** The unique identifier for the module instance **/
	String moduleID;
	
	/** The class to use to instantiate the module **/
	String fullClassName;
	
	/** Any variables that the module needs **/
	Map<String,String> variables;
	
	/** Each module will specify zero or more modules that it needs
	 * to work. Typically these represent background resoures or
	 * services. Internally, the module will look for module confs
	 * with predefined keys, e.g. 'IDFIndex' or 'TopEntities'. As
	 * these keys need not be the same as the dependancy IDs, this
	 * provides a mapping between the predefined key and module ID
	 * to use as that dependancy.
	 * */
	Map<String,String> requiredModule2Dependency;
	
	/** The list of module dependancy files to use to load othe
	 * modules that this module needs to function. */
	List<String> moduleDepandencyFiles;
	
	/** This is a list of standard frame formats that the input 
	 * frames need to implement for this module to work. */
	List<String> requiredInputFrameTypes;
	
	
	/** This is a list of standard frame formats that this module
	 * will preserve from the input in its output */
	List<String> passThroughFrameTypes;

	
	/** This is a list of standard frame formats that the output 
	 * frames will have. There may be others.  */
	List<String> newOutputFrameTypes;

	/** Logger */
	static Logger logger = LogManager.getLogger(ASPModuleConf.class);
	
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
	public ASPModuleConf(String moduleID, String fullClassName,
			Map<String, String> variables,
			Map<String, String> requiredModule2Dependency,
			List<String> moduleDepandencyFiles,
			List<String> requiredInputFrameTypes,
			List<String> passThroughFrameTypes,
			List<String> newOutputFrameTypes) {
		super();
		this.moduleID = moduleID;
		this.fullClassName = fullClassName;
		this.variables = variables;
		this.requiredModule2Dependency = requiredModule2Dependency;
		this.moduleDepandencyFiles = moduleDepandencyFiles;
		this.requiredInputFrameTypes = requiredInputFrameTypes;
		this.passThroughFrameTypes = passThroughFrameTypes;
		this.newOutputFrameTypes = newOutputFrameTypes;
	}

	/**
	 * Load this modules configuration from a JSON text file.
	 * @param confFile
	 */
	public ASPModuleConf(String confFile) {
		
		try {
			load(confFile);
		} catch (Exception e) {
			logger.log(Level.ERROR, "   ASP Module Configuration load [failed]");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/** Constructor for automatic creation */
	public ASPModuleConf() {}
	
	/**
	 * Internal load method for an ASP Module configuration file.
	 * @param confFile
	 * @throws Exception 
	 */
	public void load(String confFile) throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();
		
		ASPModuleConf confTMP = mapper.readValue(new File(confFile), ASPModuleConf.class); 
		this.moduleID = confTMP.moduleID;
		this.fullClassName = confTMP.fullClassName;
		this.variables = confTMP.getVariables();
		this.requiredModule2Dependency = confTMP.requiredModule2Dependency;
		this.moduleDepandencyFiles = confTMP.moduleDepandencyFiles;
		this.requiredInputFrameTypes = confTMP.requiredInputFrameTypes;
		this.passThroughFrameTypes = confTMP.passThroughFrameTypes;
		this.newOutputFrameTypes = confTMP.newOutputFrameTypes;
		
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

	public String getModuleID() {
		return moduleID;
	}

	public void setModuleID(String moduleID) {
		this.moduleID = moduleID;
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

	public Map<String, String> getRequiredModule2Dependency() {
		return requiredModule2Dependency;
	}

	public void setRequiredModule2Dependency(
			Map<String, String> requiredModule2Dependancy) {
		this.requiredModule2Dependency = requiredModule2Dependancy;
	}

	public List<String> getModuleDepandencyFiles() {
		return moduleDepandencyFiles;
	}

	public void setModuleDepandencyFiles(List<String> moduleDepandancyFiles) {
		this.moduleDepandencyFiles = moduleDepandancyFiles;
	}

	public List<String> getRequiredInputFrameTypes() {
		return requiredInputFrameTypes;
	}

	public void setRequiredInputFrameTypes(List<String> requiredInputFrameTypes) {
		this.requiredInputFrameTypes = requiredInputFrameTypes;
	}

	public List<String> getNewOutputFrameTypes() {
		return newOutputFrameTypes;
	}

	public void setNewOutputFrameTypes(List<String> newOutputFrameTypes) {
		this.newOutputFrameTypes = newOutputFrameTypes;
	}

	public List<String> getPassThroughFrameTypes() {
		return passThroughFrameTypes;
	}

	public void setPassThroughFrameTypes(List<String> passThroughFrameTypes) {
		this.passThroughFrameTypes = passThroughFrameTypes;
	}
	
	
	
}
