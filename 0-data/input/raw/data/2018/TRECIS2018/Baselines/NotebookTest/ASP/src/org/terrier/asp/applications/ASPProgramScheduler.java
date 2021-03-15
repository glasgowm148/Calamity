package org.terrier.asp.applications;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.terrier.asp.conf.ASPProgram;
import org.terrier.asp.core.ASPBatchUnit;
import org.terrier.asp.core.ASPExecution;
import org.terrier.asp.core.ASPStreamUnit;
import org.terrier.asp.core.ASPTemplateManager;
import org.terrier.asp.exits.ExitCheckThread;
import org.terrier.asp.exits.NewFilesAreEmptyCheckThread;
import org.terrier.asp.interfaces.StreamingModule;
import org.terrier.asp.modules.batch.ASPWriterModule;
import org.terrier.asp.interfaces.BatchModule;
import org.terrier.asp.interfaces.DatasetReader;
import org.terrier.asp.interfaces.StreamReader;

/**
 * This is the main class for ASP. It ges through seven main phases:
 *    1) Loads the ASP program provided and validates it
 *    2) Starts a new Spark (streaming) session
 *    3) Initalizes each module specified in the template in turn
 *        > Construct
 *        > Configure
 *        > Fuel
 *    4) Reconstructs the template processing as Spark operations
 *    5) Executes the Spark session
 *    6) Waits until processing completes
 *    7) Exits
 * @author richardm
 *
 */
public class ASPProgramScheduler {

	// Arguments
	/** Location of the ASP Program file to use */
	String aspProgramFile;

	// Logger
	static Logger logger;

	// State
	/** This is the internal representation of the ASP program components */
	public ASPProgram currentProgram;
	/** This is the internal template manager object that provides functionality
	 * to check that the modules defined in a program connect together correctly */
	public ASPTemplateManager templateManager;
	/** The current spark session, i.e. the driver program that submits the spark
	 * job to the cluster/local thread pool */
	public JavaStreamingContext ssc;
	/** The underlying spark session (called ASPMain) */
	public SparkSession sparkSession;
	/** The program execution (this contains the module objects that are serialized
	 *  and deployed */
	public ASPExecution programExecution;
	/** The data streams that form the final output after all of the modules have been run */
	public List<JavaDStream<Row>> unitOutputStreams;
	/** The datasets that form the final output after all of the modules have been run */
	public List<Dataset<Row>> unitOutputDatasets;
	/** As we are building a streaming application, we need an exit condition, or we
	 * will run forever */
	public ExitCheckThread exitCondition;
	
	/** Do we have one or more batch modules to run? */
	boolean thereAreBatchesToProcess = false;
	/** Do we have one or more stream modules to run? */
	boolean thereAreStreamsToProcess = false;
	
	

	/**
	 * This is the main class for the ASP platform, it configures and runs a specified
	 * ASP program.
	 * @param args
	 */
	public static void main(String[] args) {

		ASPProgramScheduler mainClass = new ASPProgramScheduler(args);

		mainClass.printHelp(); // print the header and help message, then continue
		mainClass.printArgs(); // print the input args, helps identify issues

		// Start working through the stages


		// Stage 1: Load and check the ASP Program
		mainClass.stage1_ProcessTemplating();

		// Stage 2: Start Spark
		mainClass.stage2_StartSparkSession();

		// Stage 3: Create the modules and readers
		mainClass.stage3_CreateExecution();

		// Stage 4: Convert to a series of Spark operations
		mainClass.stage4_ExpressAsSparkOperations();

		// Stage 5: Launch the spark job
		mainClass.stage5_Launch();

		// Waits until the spark job is complete
		mainClass.stage6_AwaitTermination();

	}



	/** Default Constructor */
	public ASPProgramScheduler(String[] args) {

		logger = LogManager.getLogger(ASPProgramScheduler.class);
		BasicConfigurator.configure();

		// Arguments
		aspProgramFile = args[0];
	}

	/**
	 * Prints the help message
	 */
	public void printHelp() {

		logger.log(Level.INFO, "########################################");
		logger.log(Level.INFO, "### Automatic Stream Processor (ASP) ###");
		logger.log(Level.INFO, "########################################");
		logger.log(Level.INFO, "[Help]");
		logger.log(Level.INFO, "   Command Format:");
		logger.log(Level.INFO, "      java -jar ASP_X.X.jar [ASP Program File]");
		logger.log(Level.INFO, "   Variables:");
		logger.log(Level.INFO, "      ASP Program File: Location of a file on disk containing the details of");
		logger.log(Level.INFO, "                        the program (the series of modules) to join and execute.");
		logger.log(Level.INFO, "                        (these files typically end with '.program').");
	}

	/**
	 * Prints the command line arguments for the user
	 * @param args
	 */
	public void printArgs() {

		logger.log(Level.INFO, "[ParsedArgs]");
		logger.log(Level.INFO, "   ASP Program File: '"+aspProgramFile+"'");
	}

	/**
	 * Loads the ASP configuration file specified on the command line and
	 * does some basic validation of the contents
	 * @param aspConfigurationFile
	 */
	public void stage1_ASPConfigure() {

	}

	/**
	 * Loads in an ASP program, triggers checking and loading of all named
	 * modules, then checks that the program is valid in terms of communication 
	 * between components. 
	 */
	public void stage1_ProcessTemplating() {

		logger.log(Level.INFO, "[Stage 1]");

		// initially load the program file
		currentProgram = new ASPProgram(aspProgramFile);

		// create a new ASPTemplateManager
		templateManager = new ASPTemplateManager();

		// now check that the configuration directory contains configurations
		// for each of the named recievers and modules listed in the program
		boolean validated = templateManager.validateProgram(currentProgram);
		if (!validated) {
			logger.log(Level.ERROR, "   Validation failed, check the program and configuration files");
			System.exit(1);
		}

	}

	/**
	 * Creates a new spark session based on the ASP configuration
	 */
	public void stage2_StartSparkSession() {
		logger.log(Level.INFO, "[Stage 2]");

		SparkConf conf = new SparkConf().setAppName("ASPMain").setMaster(currentProgram.getSparkMode());
		ssc = new JavaStreamingContext(conf, new Duration(5000));
		
		sparkSession = SparkSession
				.builder()
				.config(conf)
				//.config("spark.some.config.option", "some-value")
				.getOrCreate();

		sparkSession.sparkContext().setLogLevel(currentProgram.getLoggingLevel());
		
		
		// wait for a couple of seconds for the session threads to set up
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		logger.log(Level.INFO, "   Spark session initalized, mode is "+currentProgram.getSparkMode());
	}

	/**
	 * Create a the ASP Execution for the provided program. The executor 
	 * object is created and then we start to insert readers and components
	 * Module initalization is a three step process:
	 *  > Construct - create the module
	 *  > Configure - set the variables
	 *  > Fuel - load in any required resources
	 */
	public void stage3_CreateExecution() {

		logger.log(Level.INFO, "[Stage 3]");

		// We pass over to ASPExecution to load all of the modules, this
		// does both the construct and configure steps
		programExecution = new ASPExecution(currentProgram, sparkSession);

		// Fueling step: if the modules need other dependant modules then
		//               chain those in
		//TODO

	}

	/**
	 * If our ASP Execution specifies any batch processes then we need to run them first
	 */
	public void stage5_runBatchOperations() {
		logger.log(Level.INFO, "[Stage 4]");


	}

	/**
	 * converts the ASP Execution into a series of spark operations (e.g. map or
	 * transform) that can then be deployed.
	 */
	public void stage4_ExpressAsSparkOperations() {

		logger.log(Level.INFO, "[Stage 5]");

		unitOutputStreams = new ArrayList<JavaDStream<Row>>(1);
		unitOutputDatasets = new ArrayList<Dataset<Row>>(1);

		// Do Stream Units
		List<ASPStreamUnit> streamUnits = programExecution.getStreamUnits();
		if (streamUnits.size()>0) {
			thereAreStreamsToProcess = true;


			for (ASPStreamUnit streamUnit : streamUnits) {

				logger.log(Level.INFO, "   Stream Unit: ["+streamUnit.toString()+"]");

				// Part 1: Convert the list of Stream Readers to a list of JavaDStream<Row>
				JavaDStream<Row> firstInputStream = null;
				List<JavaDStream<Row>> sparkinputStreams = new ArrayList<JavaDStream<Row>>();
				for (StreamReader reader : streamUnit.getInputReaders()) {
					JavaDStream<Row> customReceiverStream = ssc.receiverStream(reader);
					if (firstInputStream==null) firstInputStream = customReceiverStream;
					else sparkinputStreams.add(customReceiverStream);
				}

				// Part 2: Merge input streams into a single stream
				JavaDStream<Row> aggregatedInputStream = null;
				if (sparkinputStreams.size()>0) ssc.union(firstInputStream, sparkinputStreams);
				else aggregatedInputStream = firstInputStream;

				logger.log(Level.INFO, "   Readers [ready]");

				// Part 3: Convert each unit
				List<JavaDStream<Row>> moduleInputOutputChain = new ArrayList<JavaDStream<Row>>();
				moduleInputOutputChain.add(aggregatedInputStream);
				for (StreamingModule streamingModule : streamUnit.getModulesToRun()) {
					moduleInputOutputChain.add(streamingModule.execute(moduleInputOutputChain.get(moduleInputOutputChain.size()-1)));
				}

				unitOutputStreams.add(moduleInputOutputChain.get(moduleInputOutputChain.size()-1));
			}


		}

		// Do Batch Units
		List<ASPBatchUnit> batchUnits = programExecution.getBatchUnits();
		if (batchUnits.size()>0) {
			thereAreBatchesToProcess = true;

			int unitID = 0;
			for (ASPBatchUnit batchUnit : batchUnits) {

				logger.log(Level.INFO, "   Stream Unit: ["+batchUnit.toString()+"]");

				// Part 1: Convert the list of Stream Readers to a list of JavaDStream<Row>
				Dataset<Row> firstInputDataset = null;
				List<Dataset<Row>> sparkinputDatasets = new ArrayList<Dataset<Row>>();
				for (DatasetReader reader : batchUnit.getInputReaders()) {
					Dataset<Row> dataset = reader.getDataset();
					if (firstInputDataset==null) firstInputDataset = dataset;
					else sparkinputDatasets.add(dataset);
				}

				// Part 2: Merge input streams into a single stream
				Dataset<Row> aggregatedInputDataset = null;
				for (Dataset<Row> otherDatasetPart : sparkinputDatasets) firstInputDataset = firstInputDataset.union(otherDatasetPart);
				aggregatedInputDataset = firstInputDataset;

				logger.log(Level.INFO, "   Readers [ready]");

				// Part 3: Convert each unit
				List<Dataset<Row>> moduleInputOutputChain = new ArrayList<Dataset<Row>>();
				List<StructType> moduleSchemas = new ArrayList<StructType>();
				moduleInputOutputChain.add(aggregatedInputDataset);
				moduleSchemas.add(aggregatedInputDataset.schema());
				for (BatchModule batchModule : batchUnit.getModulesToRun()) {
					batchModule.setInputSchema(moduleSchemas.get(moduleSchemas.size()-1));
					if (batchModule instanceof ASPWriterModule) ((ASPWriterModule)batchModule).setDatasetID("BatchUnit-"+unitID);
					moduleInputOutputChain.add(batchModule.execute(moduleInputOutputChain.get(moduleInputOutputChain.size()-1)));
					moduleSchemas.add(batchModule.getOutputSchema());
				}

				unitOutputDatasets.add(moduleInputOutputChain.get(moduleInputOutputChain.size()-1));
				unitID++;
			}


		}



		logger.log(Level.INFO, "   Processing unit and connections [ready]");


	}

	/**
	 * Launch the Spark job, this will also serialize the modules
	 */
	public void stage5_Launch() {

		logger.log(Level.INFO, "[Stage 6]");

		logger.log(Level.INFO, "   Triggering class seralization and launch");
		
		
		if (thereAreStreamsToProcess) {
			for (JavaDStream<Row> outputStream : unitOutputStreams) {
				outputStream.dstream().saveAsTextFiles(currentProgram.getWorkingDIR(), "ASPStreamOutput");
			}

			ssc.start();
		}
		
		


	}


	/**
	 * Waits until the spark job is complete
	 */
	public void stage6_AwaitTermination() {

		logger.log(Level.INFO, "[Stage 7]");

		logger.log(Level.INFO, "   Program Runniing...");

		try {
			// wait for a few seconds for some data to be output
			Thread.sleep(3000);

			if (thereAreStreamsToProcess) {
				// Instantiate the exit condition and start checking
				exitCondition = new NewFilesAreEmptyCheckThread(currentProgram, ssc);
				Thread t = new Thread(exitCondition);
				t.start();

				logger.log(Level.INFO, "   Exit condition enabled, awaiting Spark Termination...");

				ssc.awaitTermination(); // termination will be caused by the exit condition
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
			ssc.stop();
		}


		logger.log(Level.INFO, "   Spark Terminated, Shutting Down...");

	}

}
