package org.terrier.asp.core;

import java.util.List;

import org.terrier.asp.interfaces.BatchModule;
import org.terrier.asp.interfaces.DatasetReader;
import org.terrier.asp.interfaces.ProcessUnit;
/**
 * This is the implementation of a particular ASPUnit, it contains the configurations
 * for the different input streams and modules.
 * @author richardm
 *
 */
public class ASPBatchUnit implements ProcessUnit {

	/** These are the readers that provide the rows for this unit */
	List<DatasetReader> inputReaders;
	
	/** These are the configured modules to process in this unit */
	List<BatchModule> modulesToRun;

	/**
	 * Default constructor for a batch-type unit
	 * @param inputReaders
	 * @param modulesToRun
	 */
	public ASPBatchUnit(List<DatasetReader> inputReaders,
			List<BatchModule> modulesToRun) {
		super();
		this.inputReaders = inputReaders;
		this.modulesToRun = modulesToRun;
	}
	
	/** Default constructor for seralization */
	public ASPBatchUnit() {}

	public List<DatasetReader> getInputReaders() {
		return inputReaders;
	}

	public void setInputReaders(List<DatasetReader> inputReaders) {
		this.inputReaders = inputReaders;
	}

	public List<BatchModule> getModulesToRun() {
		return modulesToRun;
	}

	public void setModulesToRun(List<BatchModule> modulesToRun) {
		this.modulesToRun = modulesToRun;
	}
	
	
	
}
