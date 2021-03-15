package servicesImp;

import actors.FileAnalysisActor;
import adapter.AdapterFeatures;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import controllers.HomeController.StaticPath;
import messages.FileAnalysisMessage;
import messages.FileProcessedMessage;
import models.FilePerEvent;
import models.TweetApi;
import scala.concurrent.Await;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;


/**
 * This Service class performs the feature extraction
 */
public class ServicesImp {

    public ServicesImp() {
		super();
		// TODO Auto-generated constructor stub
	}

	// Start timer for tracking efficiency
    static long startTime = System.currentTimeMillis();


	/**
	 * Get all the json files in the subdirectories
	 * Calls parseEvent()
	 * ERROR - Concurrent modification
	 * @return
	 * @throws Exception
	 */
	public String akkaActorApi() throws Exception {

		StaticPath.tweets = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(Paths.get(StaticPath.path),2)) {

            paths.map(Path::toString).filter(f -> f.endsWith(".jsonl"))
                    .forEach(t -> {
                        try {
                            parseEvent(t);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

        } catch (Exception e) { e.printStackTrace(); }

        StringBuilder resultString = new StringBuilder();
        appendStringResult(StaticPath.tweets, resultString);

        return resultString.toString();
	}


	/**
	 * parseEvent() parses each event
	 * @param s
	 * @return resultString
	 * @throws Exception
	 */
	public static String parseEvent(String s) throws Exception {
		  StringBuilder resultString = new StringBuilder();

	        System.out.println("Parsing " + s);

	        // Create actorSystem
	        ActorSystem akkaSystem = ActorSystem.create("akkaSystem");

	        // Create the first actor based on the specified class
	        Props props = Props.create(FileAnalysisActor.class);
	        ActorRef coordinator = akkaSystem.actorOf(props);

	        // Create a message including the file path
	        FileAnalysisMessage msg = new FileAnalysisMessage(s);

	        // Process the results
	        final ExecutionContext ec = akkaSystem.dispatcher();

	        // Send a message to start processing the file.
	        // This is a synchronous call using 'ask' with a timeout.
	        Timeout timeout = new Timeout(300, TimeUnit.SECONDS); // 50 times out with embeddings
	        Future<Object> future = Patterns.ask(coordinator, msg, timeout);

	        FileProcessedMessage result =  (FileProcessedMessage) Await.result(future, timeout.duration());

	        printResults(result, s);

	        appendStringResult(StaticPath.tweets, resultString);

	        printTimer(startTime);

	        return resultString.toString();
	}

	/**
	 * concatenate the "intList"  json in "Result"
	 * @param intList
	 * @param resultString
	 */
    private static void appendStringResult(List<String> intList, StringBuilder resultString) {
		intList.forEach(ele-> resultString.append(ele).append("\n"));
}

	/**
	 *
	 * @param result
	 * @param fileName
	 * @throws JsonProcessingException
	 */
	private static void printResults(final FileProcessedMessage result, final String fileName) throws JsonProcessingException {
		List<TweetApi> eventFile = new ArrayList<>();
		result.getHMap().forEach(outputs -> outputs.getTweets().forEach(output -> {
			try {
				eventFile.add(AdapterFeatures.adapterTweet(output));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

		}));

		FilePerEvent fileEvent = new FilePerEvent();
		fileEvent.setFileName(fileName);
		fileEvent.setTweets(eventFile);

   	    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String r = ow.writeValueAsString(fileEvent);

		StaticPath.tweets.add(r);
	}

	/**
	 *
	 * @param startTime
	 */
	private static void printTimer(long startTime) {
		// Outputs the elapsed time to console
		long elapsedTime = System.currentTimeMillis() - startTime;
		long elapsedSeconds = elapsedTime / 1000;
		long elapsedMinutes = elapsedSeconds / 60;
		System.out.println("Time elapsed: " + elapsedMinutes + " minutes");
		System.out.println(elapsedSeconds + " seconds");
	}


	/**
	 *
	 * @param result
	 */
	public void saveResultInFile(final String result) {
		
		   try {
	            File jsonFile = new File(StaticPath.saveFile);

	            FileWriter fw = new FileWriter(jsonFile);
	            fw.write(result);
	            fw.close();

	        } catch (IOException iox) {
	            //do stuff with exception
	            iox.printStackTrace();
	        }

	}

	/**
	 *
	 * @param path
	 * @return
	 */
	public String contentSavedFile(final String path) {
		String content = "";  
		try
		{
		    content = new String ( Files.readAllBytes( Paths.get(path)));
		} 
		catch (IOException e) 
		{
		    e.printStackTrace();
		}
		return content;
	}

}
