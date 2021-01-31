package controllers;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import Utils.infoRepository;
import Utils.inputOutput;
import akka.actor.*;

// Play
import models.Tweet;
import play.mvc.Controller;
import play.mvc.Result;

// Project imports
import actors.*;
import logic.*;
import scala.compat.java8.FutureConverters;
import twitter4j.TwitterException;
//import views.html.resultPage;

import javax.inject.Inject;

import static actors.jsonReader.getMin;
import static akka.pattern.Patterns.ask;




public class HomeController extends Controller {

    private static final List<Tweet> tweetList  = new ArrayList<>();
    private final String path = "../../0-data/raw/data/2020/2020-A/tweets/athens_earthquake";
    private final String output_file = "brand_new_run3";


    // Define Actor References
    private final ActorRef event_actor;
    private final ActorRef sentiment_actor;

    // Initialise the Actor references using dependency injection
    @Inject
    public HomeController(ActorSystem actorObj) {
        event_actor = actorObj.actorOf(eventActor.getProps());
        sentiment_actor = actorObj.actorOf(eventActor.getProps());
    }


    // index() is triggered on GET to localhost:9000/
    public Result index()  {


        // Start timer for tracking efficiency
        long startTime = System.currentTimeMillis();

        // Parse into a Tweet model
        //jsonReader reader = new jsonReader();
        //reader.parse();

        List<Tweet> tweetList = jsonActor();
        tweetList = sentiActor(tweetList);

        // Uncomment this line to parse all tweets (resource intensive! - hours)
        //tweetList = reader.parseOne(pathAll);


        /**
         * Main Logic done
         * Print the vector, timer and results.
         */

        inputOutput.printVector(output_file, tweetList);

        printTimer(startTime);

        return ok(Objects.requireNonNull(inputOutput.VectorToPrettyFormat(new File(output_file))));         //return ok(Sanitise.toPrettyFormat(new File("../../1-src/1-java/conf/10.jsonl")));
    }

    private void printTimer(long startTime) {
        // Outputs the elapsed time to console
        long elapsedTime = System.currentTimeMillis() - startTime;
        long elapsedSeconds = elapsedTime / 1000;
        long elapsedMinutes = elapsedSeconds / 60;
        System.out.println("Time elapsed: " + elapsedMinutes + " minutes");
        System.out.println(elapsedSeconds + " seconds");
    }

    /**
     * This method passes each file within the specified directory to parseEvent()
     * @return tweetList
     * @calls jsonReader.parseEvent -> jsonReader.
     */
    private List<Tweet> jsonActor() {
        try (Stream<Path> paths = Files.walk(Paths.get(path))) { //tweets/athens_earthquake  //testy
            paths.filter(Files::isRegularFile).forEach(jsonReader::parseEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<Tweet> sentiActor(List<Tweet> tweetList){

        // Instantiate a new featureActor()
        featureActor featureActor = new featureActor();

        // getKeywords gets the TFIDF
        featureActor.getKeywords(HomeController.tweetList);

        SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer();
        sentimentAnalyzer.initialize();

        // Offset + Sentiment + TwitterText + Glove
        try {
            jsonReader.tweetAnalyser(getMin(),  sentimentAnalyzer); //model,
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return tweetList;




    }
    /*
    public CompletionStage<Result> resultEvent(String name) throws TwitterException, ExecutionException, InterruptedException {
        return FutureConverters.toJava(ask(event_actor, new eventActor.parse("/test.jsonl"), 5000))
                .thenApplyAsync(userInfo -> ok(resultPage.render((infoRepository) userInfo)));
    }

     */



}

