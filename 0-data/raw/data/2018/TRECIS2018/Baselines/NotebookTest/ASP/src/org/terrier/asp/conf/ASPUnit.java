package org.terrier.asp.conf;

import java.io.Serializable;
import java.util.List;

/**
 * An ASP unit is a processing component that is comprised of one or
 * more modules that need to be processed together. The most common use
 * cases for this are where some pre-processing needs to be performed
 * to create a separate data structure before another module can run or
 * where multiple modules each feed until the same output stream.  
 * @author richardm
 *
 */
public class ASPUnit implements Serializable {

	private static final long serialVersionUID = 4956473491095896699L;

	/** A name for this unit */
	String unitName; 
	
	/** An explanation of what this unit does. */
	String unitExpanation;
	
	/** What type of unit is this, batch or stream? */
	ASPUnitType unitType;
	
	/** This is the list of reader configuration files that provide data input */
	List<String> unitInputReaders;
	
	/** This is the list of configuration files that define the processing pipeline */
	List<String> unitModulePipeline;

	
	/**
	 * Deafult constructore for an ASP Unit, called from ASPProgram.
	 * @param unitType
	 * @param unitModulePipeline
	 * @param unitModuleConnectors
	 */
	public ASPUnit(ASPUnitType unitType, List<String> unitInputReaders, List<String> unitModulePipeline, String unitName) {
		super();
		this.unitType = unitType;
		this.unitInputReaders = unitInputReaders;
		this.unitModulePipeline = unitModulePipeline;
		this.unitName = unitName;
	}
	
	/** Constructor for automatic creation */
	public ASPUnit() {}

	public List<String> getUnitModulePipeline() {
		return unitModulePipeline;
	}

	public void setUnitModulePipeline(List<String> unitModulePipeline) {
		this.unitModulePipeline = unitModulePipeline;
	}

	public ASPUnitType getUnitType() {
		return unitType;
	}

	public void setUnitType(ASPUnitType unitType) {
		this.unitType = unitType;
	}

	public List<String> getUnitInputReaders() {
		return unitInputReaders;
	}

	public void setUnitInputReaders(List<String> unitInputReaders) {
		this.unitInputReaders = unitInputReaders;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getUnitExpanation() {
		return unitExpanation;
	}

	public void setUnitExpanation(String unitExpanation) {
		this.unitExpanation = unitExpanation;
	}
	
	public String explain() {
		return unitExpanation;
	}
	
}
