package org.terrier.asp.applications;

import org.apache.spark.sql.SparkSession;
import org.terrier.asp.conf.ASPModuleConf;
import org.terrier.asp.conf.ASPReaderConf;
import org.terrier.asp.interfaces.BatchModule;
import org.terrier.asp.interfaces.DatasetReader;
import org.terrier.asp.interfaces.StreamReader;
import org.terrier.asp.interfaces.StreamingModule;

public class ASPStaticUtils {

	/**
	 * Load an ASP Batch Module from a configuration file
	 * @param confFile
	 * @return
	 */
	public static BatchModule getBatchModule(String confFile) {
		
		ASPModuleConf moduleConf = new ASPModuleConf(confFile);
		
		BatchModule batchModule = null;
		try {
			batchModule = (BatchModule) Class.forName(moduleConf.getFullClassName()).getDeclaredConstructor(ASPModuleConf.class).newInstance(moduleConf);
		} catch (Exception e) {
			System.err.println("      Failed creating module "+moduleConf.getModuleID());
			e.printStackTrace();
		}
		return batchModule;
	}
	
	/**
	 * Load an ASP Streaming Module from a configuration file
	 * @param confFile
	 * @return
	 */
	public static StreamingModule getStreamingModule(String confFile) {
		
		ASPModuleConf moduleConf = new ASPModuleConf(confFile);
		
		StreamingModule streamingModule = null;
		try {
			streamingModule = (StreamingModule) Class.forName(moduleConf.getFullClassName()).getDeclaredConstructor(ASPModuleConf.class).newInstance(moduleConf);
		} catch (Exception e) {
			System.err.println("      Failed creating module "+moduleConf.getModuleID());
			e.printStackTrace();
		}
		return streamingModule;
	}
	
	/**
	 * Load an ASP Dataset Reader from a configuration file
	 * @param confFile
	 * @param currentSession
	 * @return
	 */
	public static DatasetReader getDatasetReader(String confFile, SparkSession currentSession) {

		ASPReaderConf conf = new ASPReaderConf(confFile);
		
		// create the module
		DatasetReader readerObject = null;
		try {
			readerObject = (DatasetReader) Class.forName(conf.getFullClassName()).getDeclaredConstructor(ASPReaderConf.class, SparkSession.class).newInstance(conf, currentSession);
		} catch (Exception e) {
			System.err.println("            Failed creating reader "+confFile);
			e.printStackTrace();
		}
		
		return readerObject;
	}
	
	/**
	 * Load an ASP Stream Reader from a configuration file
	 * @param confFile
	 * @param currentSession
	 * @return
	 */
	public static StreamReader getStreamReader(String confFile, SparkSession currentSession) {

		ASPReaderConf conf = new ASPReaderConf(confFile);
		
		// create the module
		StreamReader readerObject = null;
		try {
			readerObject = (StreamReader) Class.forName(conf.getFullClassName()).getDeclaredConstructor(ASPReaderConf.class, SparkSession.class).newInstance(conf, currentSession);
		} catch (Exception e) {
			System.err.println("            Failed creating reader "+confFile);
			e.printStackTrace();
		}
		
		return readerObject;
	}
	
}
