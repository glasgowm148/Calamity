package org.terrier.asp.core;

import java.util.List;

import org.terrier.asp.interfaces.ProcessUnit;
import org.terrier.asp.interfaces.StreamReader;
import org.terrier.asp.interfaces.StreamingModule;

/**
 * This is the implementation of a particular ASPUnit, it contains the configurations
 * for the different input streams and modules.
 * @author richardm
 *
 */
public class ASPStreamUnit implements ProcessUnit {

	/** These are the readers that provide the rows for this unit */
	List<StreamReader> inputReaders;
	
	/** These are the configured modules to process in this unit */
	List<StreamingModule> modulesToRun;

	/**
	 * Default constructor for a stream-type unit
	 * @param inputReaders
	 * @param modulesToRun
	 */
	public ASPStreamUnit(List<StreamReader> inputReaders,
			List<StreamingModule> modulesToRun) {
		super();
		this.inputReaders = inputReaders;
		this.modulesToRun = modulesToRun;
	}
	
	/** Default constructor for seralization */
	public ASPStreamUnit() {}

	public List<StreamReader> getInputReaders() {
		return inputReaders;
	}

	public void setInputReaders(List<StreamReader> inputReaders) {
		this.inputReaders = inputReaders;
	}

	public List<StreamingModule> getModulesToRun() {
		return modulesToRun;
	}

	public void setModulesToRun(List<StreamingModule> modulesToRun) {
		this.modulesToRun = modulesToRun;
	}
	
	
	
}