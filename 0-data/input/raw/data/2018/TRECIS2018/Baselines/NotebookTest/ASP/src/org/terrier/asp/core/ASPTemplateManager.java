package org.terrier.asp.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.terrier.asp.conf.ASPModuleConf;
import org.terrier.asp.conf.ASPProgram;
import org.terrier.asp.conf.ASPReaderConf;
import org.terrier.asp.conf.ASPUnit;

/**
 * The template manager is responsible for checking that an ASP program is
 * executable, i.e. all of its components will connect together in terms of
 * inputs and outputs.
 * @author richardm
 *
 */
public class ASPTemplateManager {


	/** Logger */
	static Logger logger = LogManager.getLogger(ASPTemplateManager.class);

	/**
	 * The constructor for the Template manager. It takes in ASP
	 * configuration so we can access the parameters such as the
	 * configuration directory.
	 * @param mainConfiguration
	 */
	public ASPTemplateManager() {
	}

	/**
	 * Runs a series of validation checks on the provided ASPProgram
	 * with the aim of checking that each module can be loaded ok and
	 * they all connect together correctly.
	 * 
	 * Things checked:
	 *  - Do the configuration files exist
	 *  - Are all named readers/modules unique?
	 *  - Do the named classes exist?
	 *  - Do the ouput types for each unit match the input types for
	 *    the next unit
	 *    
	 * @param currentProgram
	 * @return
	 */
	public boolean validateProgram(ASPProgram currentProgram) {

		logger.log(Level.INFO, "   Program validation [started]");

		// A set of all reader/module ids seen so far
		Set<String> observedModuleIDs = new HashSet<String>();
		Set<String> commonOutputTypes = new HashSet<String>();


		logger.log(Level.INFO, "   Checking processing units...");
		int u = 0;
		for (ASPUnit unit : currentProgram.getModulePipeline()) {

			logger.log(Level.INFO, "      Unit "+u+": "+unit.getUnitName()+" [check]");


			// Unit check state
			Set<String> currentProvidedOutputTypes = new HashSet<String>();

			// Start with the readers
			logger.log(Level.INFO, "      Checking readers...");

			// now check the module's reader classes
			for (String reader : unit.getUnitInputReaders()) {

				// check file
				String filename = reader;
				File fileObject = new File(filename);
				if (!fileObject.exists()) {
					logger.log(Level.ERROR, "         "+filename+" does not exist");
					return false;
				}

				// load file (will report internally if load fails)
				ASPReaderConf conf = new ASPReaderConf(filename);

				// check module ID
				if (observedModuleIDs.contains(conf.getReaderID())) {
					logger.log(Level.ERROR, "         Duplicate id "+conf.getReaderID()+", readers/module ids must be unique");
					return false;
				}
				observedModuleIDs.add(conf.getReaderID());

				// check that the class exists
				try {
					Class.forName( conf.getFullClassName() );
				} catch( ClassNotFoundException e ) {
					logger.log(Level.ERROR, "         Named reader class "+conf.getFullClassName()+", does not exist");
					return false;
				}

				// Update the common reader output types
				if (commonOutputTypes.size()==0) commonOutputTypes.addAll(conf.getGuaranteedOutputFrameTypes());
				else {
					Set<String> unsupportedReaderTypes = new HashSet<String>();
					for (String outputReaderType : commonOutputTypes) {
						if (!conf.getGuaranteedOutputFrameTypes().contains(outputReaderType)) unsupportedReaderTypes.add(outputReaderType);
					}
					for (String unsupportedType : unsupportedReaderTypes) commonOutputTypes.remove(unsupportedType);

					if (commonOutputTypes.size()==0) {
						logger.log(Level.ERROR, "         Type check [failed]: There are no common output types for the specified readers");
						return false;
					}
				}

				logger.log(Level.INFO, "         Reader "+conf.getReaderID()+" [validated]");

			}

			StringBuilder textDescPutTypes = new StringBuilder();
			for (String s : commonOutputTypes) {
				if (textDescPutTypes.length()!=0)textDescPutTypes.append(", ");
				textDescPutTypes.append(s);
			}
			logger.log(Level.INFO, "   Readers emit the following common types: ["+textDescPutTypes.toString()+"]");

			// for each module in the unit
			for (int pipePos = 0; pipePos<unit.getUnitModulePipeline().size(); pipePos++) {

				String moduleFile = unit.getUnitModulePipeline().get(pipePos);

				// check file
				String filename = moduleFile;
				File fileObject = new File(filename);
				if (!fileObject.exists()) {
					logger.log(Level.ERROR, "      "+filename+" does not exist");
					return false;
				}

				// Load module file
				ASPModuleConf moduleConf = new ASPModuleConf(filename);

				// check module ID
				if (observedModuleIDs.contains(moduleConf.getModuleID())) {
					logger.log(Level.ERROR, "      Duplicate id "+moduleConf.getModuleID()+", readers/module ids must be unique");
					return false;
				}
				observedModuleIDs.add(moduleConf.getModuleID());

				// check that the class exists
				try {
					Class.forName( moduleConf.getFullClassName() );
				} catch( ClassNotFoundException e ) {
					logger.log(Level.ERROR, "      Named module class "+moduleConf.getFullClassName()+", does not exist");
					return false;
				}

				// now check that the module inputs match
				for (String inputType : moduleConf.getRequiredInputFrameTypes()) {
					if (!commonOutputTypes.contains(inputType)) {
						logger.log(Level.ERROR, "      Type check [failed]: Module "+moduleConf.getModuleID()+" expects input of type "+inputType+" but the input stream does not guarantee that type");
						return false;
					}
				}

				// now check the connectors to see if we need to update the expected output types
				Set<String> previousProvidedOutputTypes = new HashSet<String>(currentProvidedOutputTypes.size());
				if (pipePos==0) previousProvidedOutputTypes.addAll(commonOutputTypes);
				else previousProvidedOutputTypes.addAll(currentProvidedOutputTypes);
				currentProvidedOutputTypes.clear();
				currentProvidedOutputTypes.addAll(moduleConf.getNewOutputFrameTypes());
				
				if (moduleConf.getPassThroughFrameTypes().size()==1 && moduleConf.getPassThroughFrameTypes().get(0).equalsIgnoreCase("*")) currentProvidedOutputTypes.addAll(previousProvidedOutputTypes);
				else currentProvidedOutputTypes.addAll(moduleConf.getPassThroughFrameTypes());
				

				if (currentProvidedOutputTypes.size()==0) {
					logger.log(Level.ERROR, "      Type check [failed]: There are no output types for "+moduleConf.getModuleID()+". Modules need to either pass-through frames or define new output frames.");
					return false;
				}

				logger.log(Level.INFO, "         Module "+moduleConf.getModuleID()+" [validated]");
				
				commonOutputTypes = currentProvidedOutputTypes;
			}

			textDescPutTypes = new StringBuilder();
			for (String s : currentProvidedOutputTypes) {
				if (textDescPutTypes.length()!=0)textDescPutTypes.append(", ");
				textDescPutTypes.append(s);
			}
			logger.log(Level.INFO, "         Unit "+u+" [validated], this unit emits the following common types: ["+textDescPutTypes+"]");


			commonOutputTypes.clear();
			commonOutputTypes.addAll(currentProvidedOutputTypes);
			u++;
		}
		logger.log(Level.INFO, "   Validation [complete]");
		return true;
	}

}
