package org.terrier.asp.conf;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * An ASP program represents the sequence of modules that need to be run
 * to complete the task. This ASPProgram class represents the physical file
 * that contains the definition of the sequence of modules to run. 
 * 
 * ASPProgram is loaded by the ASPTemplateManager.
 * @author richardm
 *
 */
public class ASPProgram implements Serializable{

	private static final long serialVersionUID = 8196281317812787653L;

	/** Some ASP programs will write data to local disk during opperation,
	 * This parameter specifies in what folder to write this information.*/
	String workingDIR;
	
	/** This is the spark configuration mode for running the program */
	String sparkMode;
	
	/** Logging level for the program */
	String loggingLevel = "INFO";
	
	/** This is the list of processing units to run in sequence */
	List<ASPUnit> modulePipeline;
	
	/** Logger */
	static Logger logger = LogManager.getLogger(ASPProgram.class);
	
	/**
	 * Default loader for an ASP program, reads a program to run from file
	 * @param programFile
	 */
	public ASPProgram(String programFile) {
		try {
			load(programFile);
		} catch (Exception e) {
			logger.log(Level.ERROR, "   ASP Program load [failed]");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	
	/**
	 * Alternate Constructor, used for testing
	 * @param workingDIR
	 * @param modulePipeline
	 */
	public ASPProgram(String workingDIR, String sparkMode, List<ASPUnit> modulePipeline, String loggingLevel) {
		super();
		this.modulePipeline = modulePipeline;
		this.workingDIR = workingDIR;
		this.sparkMode = sparkMode;
		this.loggingLevel = loggingLevel;
	}

	/** Constructor for automatic creation */
	public ASPProgram() {}

	/**
	 * Internal load method for an ASP Program from file.
	 * @param programFile
	 * @throws Exception 
	 */
	public void load(String programFile) throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		
		ASPProgram confTMP = mapper.readValue(new File(programFile), ASPProgram.class); 
		this.modulePipeline = confTMP.modulePipeline;
		this.workingDIR = confTMP.workingDIR;
		this.sparkMode = confTMP.sparkMode;
		this.loggingLevel = confTMP.loggingLevel;
		
		logger.log(Level.INFO, "   ASP Program [loaded]");
		
	}
	
	/**
	 * Internal save method allowing for the contents of this
	 * configuration to be written to disk.
	 * @param confFile
	 * @throws Exception 
	 */
	public void save(String programFile) throws Exception {
		
		
		ObjectMapper mapper = new ObjectMapper();

		//Object to JSON in file
		mapper.writerWithDefaultPrettyPrinter().writeValue(new File(programFile), this);
		
	}

	public List<ASPUnit> getModulePipeline() {
		return modulePipeline;
	}

	public void setModulePipeline(List<ASPUnit> modulePipeline) {
		this.modulePipeline = modulePipeline;
	}


	public String getWorkingDIR() {
		return workingDIR;
	}


	public void setWorkingDIR(String workingDIR) {
		this.workingDIR = workingDIR;
	}
	
	
	public String getSparkMode() {
		return sparkMode;
	}


	public void setSparkMode(String sparkMode) {
		this.sparkMode = sparkMode;
	}


	public String getLoggingLevel() {
		return loggingLevel;
	}


	public void setLoggingLevel(String loggingLevel) {
		this.loggingLevel = loggingLevel;
	}


	public String explain() {
		StringBuilder builder = new StringBuilder();
		builder.append("This program will run "+modulePipeline+" computation units sequentially:");
		builder.append("\n");
		int i =0;
		for (ASPUnit unit : modulePipeline) {
			builder.append("Unit "+i);
			builder.append(":   ");
			builder.append(unit.explain());
			builder.append("\n");
			i++;
		}
		return builder.toString();
	}
	
	
	
	
	
}
