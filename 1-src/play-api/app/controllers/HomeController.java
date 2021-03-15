package controllers;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import Utils.FileUtils;
import play.mvc.Controller;
import play.mvc.Result;
import servicesImp.ServicesImp;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
 

/**
 * This controller contains an action to handle HTTP requests to the
 * application's home page.
 */
public class HomeController extends Controller {

	private ServicesImp service = new ServicesImp();

	public static class StaticPath {

		public static List<String> tweets = new ArrayList<>();
		public static String path = "data/tweets";
		public static String output_file = "new_with_offset";
		public static String saveFile = "data/savedFile.json";

	}

	// Start timer for tracking efficiency
	static long startTime = System.currentTimeMillis();

	/* Entry point for /tweets */
	public Result index() throws Exception {

		//call akka actor service
		String result = service.akkaActorApi();

		// save result in file
		service.saveResultInFile(result);

		return ok(result);
	}

	/**
	 * this function allows you to read the result from a file, 
	 * to avoid calling on / tweets which are very expensive
	 * @return
	 */
	public Result explore() {	
		// read the file that contains the result of / tweet 
		return ok(service.contentSavedFile(StaticPath.saveFile));	
	}


	

	public Result tutorial() {
		return ok("");
	}

}
