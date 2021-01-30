package controllers;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import Utils.infoRepository;
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
import views.html.resultPage;

import static actors.jsonReader.getMin;
import static akka.pattern.Patterns.ask;

public class HomeController extends Controller {


    private static final List<Tweet> tweetList  = new ArrayList<>();
    private Object sentimentActor;

    public HomeController(ActorRef sentimentActor) {
    }


    // index() is triggered on GET to localhost:9000/
    public Result index()  {

        // Start timer for tracking efficiency
        long startTime = System.currentTimeMillis();

        // Parse into a Tweet model
        //jsonReader reader = new jsonReader();
        //reader.parse();

        List<Tweet> tweetList = jsonActor();
        sentiActor(tweetList);
        // Uncomment this line to parse all tweets (resource intensive! - hours)
        //tweetList = reader.parseOne(pathAll);

        // Print elapsed time to console
        printTimer(startTime);

        // Prints to browser
        return ok(Sanitise.toPrettyFormat(new File("../../1-src/1-java/conf/10.jsonl")));
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
     * @return
     */
    private List<Tweet> jsonActor() {
        try (Stream<Path> paths = Files.walk(Paths.get("../../0-data/raw/data/2020/2020-A/tweets/athens_earthquake"))) { //tweets/athens_earthquake  //testy
            paths.filter(Files::isRegularFile).forEach(jsonReader::parseEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void sentiActor(List<Tweet> tweetList){

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


        jsonReader.printVector("brand_new_run2");


    }


    public CompletionStage<Result> resultEvent(String name) throws TwitterException, ExecutionException, InterruptedException {
        return FutureConverters.toJava(ask((ActorRef) eventActor, new eventActor.parse(name), 5000))
                .thenApplyAsync(userInfo -> ok(resultPage.render((infoRepository) userInfo)));
    }



}

