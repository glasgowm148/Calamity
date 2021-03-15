package org.terrier.asp.exits;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.terrier.asp.conf.ASPProgram;
import org.terrier.asp.core.ASPTemplateManager;

/**
 * This is a background thread type that checks whether a condition is met
 * and if so calls a graceful halt to the Spark session
 * @author richardm
 *
 */
public abstract class ExitCheckThread implements Runnable{

	/** The program configuration that is currently running */
	ASPProgram currentProgram;
	
	/** The Spark Session hook that we can use to end the session */
	JavaStreamingContext ssc;
	
	/** Logger */
	static Logger logger = LogManager.getLogger(ExitCheckThread.class);
	
	public ExitCheckThread(ASPProgram currentProgram, JavaStreamingContext ssc) {
		this.currentProgram =currentProgram;
		this.ssc = ssc;
	}
	
}
