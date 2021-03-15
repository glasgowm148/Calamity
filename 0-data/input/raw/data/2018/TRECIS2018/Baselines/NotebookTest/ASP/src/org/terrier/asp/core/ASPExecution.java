package org.terrier.asp.core;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.sql.SparkSession;
import org.terrier.asp.conf.ASPModuleConf;
import org.terrier.asp.conf.ASPProgram;
import org.terrier.asp.conf.ASPReaderConf;
import org.terrier.asp.conf.ASPUnit;
import org.terrier.asp.conf.ASPUnitType;
import org.terrier.asp.interfaces.BatchModule;
import org.terrier.asp.interfaces.DatasetReader;
import org.terrier.asp.interfaces.ProcessUnit;
import org.terrier.asp.interfaces.StreamingModule;
import org.terrier.asp.interfaces.StreamReader;

public class ASPExecution {

	
	/**
	 * An execution is a joined series of computation units that
	 * are comprised of modules. This is where the units and modules
	 * are stored.
	 */
	List<ProcessUnit> unitsAndModules;
	
	/** Logger */
	static Logger logger = LogManager.getLogger(ASPExecution.class);
	
	/**
	 * Constructor for an ASP execution. It takes as input an ASPProgram
	 * and ASPConfiguration and loads all of the readers and modules.
	 */
	public ASPExecution(ASPProgram program, SparkSession currentSession) {
		
		// create and configure units - this does both stream and batch units
		loadUnitsAndModules(program, currentSession);
		
	}
	

	
	/**
	 * Load each of the modules in each unit named in the program in order and configure them
	 * @param configuration
	 * @param program
	 */
	public void loadUnitsAndModules(ASPProgram program, SparkSession currentSession) {
		
		unitsAndModules = new ArrayList<ProcessUnit>();
		
		logger.log(Level.INFO, "   Creation and configuration of units and modules has [started]");
		
		// work iteratively over the units
		int u = 0;
		for (ASPUnit unit : program.getModulePipeline()) {
			
			logger.log(Level.INFO, "      Preparing unit "+u+"...");
			
			if (unit.getUnitType()==ASPUnitType.batch) {
				logger.log(Level.INFO, "      Unit is of Batch type");
				
				// configured modules in this unit
				List<BatchModule> batchModules = new ArrayList<BatchModule>(unit.getUnitModulePipeline().size());
				
				// for each module in the unit
				for (int pipePos = 0; pipePos<unit.getUnitModulePipeline().size(); pipePos++) {
								
					String moduleFile = unit.getUnitModulePipeline().get(pipePos);
					
					
					String filename = moduleFile;
					ASPModuleConf moduleConf = new ASPModuleConf(filename);
					
					BatchModule batchModule = null;
					try {
						batchModule = (BatchModule) Class.forName(moduleConf.getFullClassName()).getDeclaredConstructor(ASPModuleConf.class).newInstance(moduleConf);
					} catch (Exception e) {
						logger.log(Level.ERROR, "      Failed creating module "+moduleConf.getModuleID());
						e.printStackTrace();
						System.exit(1);
					}
					
					batchModules.add(batchModule);
					
					logger.log(Level.INFO, "         Module "+moduleConf.getModuleID()+" [created and configured]");
					
				}
				
				logger.log(Level.INFO, "      Creation and configuration of readers has [started]");
				
				List<DatasetReader> inputReaders = new ArrayList<DatasetReader>();
				
				logger.log(Level.INFO, "         Processing Batch Readers...");
				
				for (String reader : unit.getUnitInputReaders()) {
					
					logger.log(Level.INFO, "            Preparing reader "+reader);
					
					// load module configuration
					String filename = reader;
					ASPReaderConf conf = new ASPReaderConf(filename);
					
					// create the module
					DatasetReader readerObject = null;
					try {
						readerObject = (DatasetReader) Class.forName(conf.getFullClassName()).getDeclaredConstructor(ASPReaderConf.class, SparkSession.class).newInstance(conf, currentSession);
					} catch (Exception e) {
						logger.log(Level.ERROR, "            Failed creating reader "+reader);
						e.printStackTrace();
						System.exit(1);
					}
					
					inputReaders.add(readerObject);
					
				}
				
				
				unitsAndModules.add(new ASPBatchUnit(inputReaders, batchModules));	
				
			}
			
			if (unit.getUnitType()==ASPUnitType.stream) {
				logger.log(Level.INFO, "      Unit is of Stream type");
				
				
				// configured modules in this unit
				List<StreamingModule> streamingModules = new ArrayList<StreamingModule>(unit.getUnitModulePipeline().size());
				
				// for each module in the unit
				for (int pipePos = 0; pipePos<unit.getUnitModulePipeline().size(); pipePos++) {
								
					String moduleFile = unit.getUnitModulePipeline().get(pipePos);
					
					
					String filename = moduleFile;
					ASPModuleConf moduleConf = new ASPModuleConf(filename);
					
					StreamingModule streamingModule = null;
					try {
						streamingModule = (StreamingModule) Class.forName(moduleConf.getFullClassName()).getDeclaredConstructor(ASPModuleConf.class).newInstance(moduleConf);
					} catch (Exception e) {
						logger.log(Level.ERROR, "      Failed creating module "+moduleConf.getModuleID());
						e.printStackTrace();
						System.exit(1);
					}
					
					streamingModules.add(streamingModule);
					
					logger.log(Level.INFO, "         Module "+moduleConf.getModuleID()+" [created and configured]");
					
				}
				
				logger.log(Level.INFO, "      Creation and configuration of readers has [started]");
				
				List<StreamReader> inputReaders = new ArrayList<StreamReader>();
				
				logger.log(Level.INFO, "         Processing Stream Readers...");
				
				for (String reader : unit.getUnitInputReaders()) {
					
					logger.log(Level.INFO, "            Preparing reader "+reader);
					
					// load module configuration
					String filename = reader;
					ASPReaderConf conf = new ASPReaderConf(filename);
					
					// create the module
					StreamReader readerObject = null;
					try {
						readerObject = (StreamReader) Class.forName(conf.getFullClassName()).getDeclaredConstructor(ASPReaderConf.class).newInstance(conf);
					} catch (Exception e) {
						logger.log(Level.ERROR, "            Failed creating reader "+reader);
						e.printStackTrace();
						System.exit(1);
					}
					
					inputReaders.add(readerObject);
					
				}
				
				
				unitsAndModules.add(new ASPStreamUnit(inputReaders, streamingModules));	
			}
			
			
			
			
		}
	}
	
	/**
	 * Gets all of the Stream-type units in this execution
	 * @return
	 */
	public List<ASPStreamUnit> getStreamUnits() {
		
		List<ASPStreamUnit> streamUnitsFound = new ArrayList<ASPStreamUnit>();
		for (ProcessUnit unit : unitsAndModules) {
			if (unit instanceof ASPStreamUnit) streamUnitsFound.add((ASPStreamUnit)unit);
		}
		return streamUnitsFound;
	}
	
	/**
	 * Gets all of the Stream-type units in this execution
	 * @return
	 */
	public List<ASPBatchUnit> getBatchUnits() {
		
		List<ASPBatchUnit> batchUnitsFound = new ArrayList<ASPBatchUnit>();
		for (ProcessUnit unit : unitsAndModules) {
			if (unit instanceof ASPBatchUnit) batchUnitsFound.add((ASPBatchUnit)unit);
		}
		return batchUnitsFound;
	}
	
	
}
