package controllers;

import actors.featureActor;
import actors.semanticActor;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Scheduler;
import akka.actor.typed.javadsl.AskPattern;
import logic.*;
import models.Tweet;
import play.mvc.Controller;
import play.mvc.Result;
import services.CounterActor;
import services.CounterActor.Command;
import services.CounterActor.GetValue;
import services.CounterActor.Increment;

import javax.inject.Inject;
import java.io.*;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletionStage;

import tweetfeatures.NumericTweetFeatures;

import static tweetfeatures.NumericTweetFeatures.makeFeatureVector;
import actors.jsonReader;

/**
 * This controller contains an action to handle HTTP requests
 *
 * I've used an Akka-play seed,
 * the Akka cluster functionality is ignored/bypassed for now
 *
 *
 * !! To recompile, navigate to browser and refresh localhost:9000
 *
 */

public class HomeController extends Controller {
    private final File path = new File("/Users/mark/Documents/GitHub/HelpMe-clone/1-src/1-java/conf/10.jsonl");

    private final ActorRef<Command> counterActor; // , TweetActor

    private final Scheduler scheduler;

    private final Duration askTimeout = Duration.ofSeconds(3L);


    List<Tweet> tweetList = new ArrayList<>();
    List<Vector> featureVectorList = new ArrayList<>();


    @Inject
    public HomeController(ActorRef<CounterActor.Command> counterActor, Scheduler scheduler) {
        //TweetActor = system.actorOf(tweetActor.props());
        this.counterActor = counterActor;
        this.scheduler = scheduler;
    }


    public CompletionStage<Result> index() {
        // https://www.playframework.com/documentation/2.8.x/AkkaTyped#Using-the-AskPattern-&-Typed-Scheduler
        return AskPattern.ask(
                counterActor,
                GetValue::new,
                askTimeout,
                scheduler)
                .thenApply(this::renderIndex);
    }

    public CompletionStage<Result> increment() {
        // https://www.playframework.com/documentation/2.8.x/AkkaTyped#Using-the-AskPattern-&-Typed-Scheduler
        return AskPattern.ask(
                counterActor,
                Increment::new,
                askTimeout,
                scheduler)
                .thenApply(this::renderIndex);
    }


    private Result renderIndex(Integer hitCounter)  {
        long startTime = System.currentTimeMillis();


        //  sbt -java-home /Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home

        /** Parse into Tweet.class **/
        jsonReader reader = new jsonReader();
        tweetList = reader.parseOne();

        //tweetList = reader.parseAll();
        System.out.println("\ntweetList.size():\n" + tweetList.size());

        IntSummaryStatistics summaryStatistics = tweetList.stream()
                .map(Tweet::getCreatedAtStr)
                .mapToInt(Integer::parseInt)
                .summaryStatistics();

        int max = summaryStatistics.getMax();
        int min = summaryStatistics.getMin();
        System.out.println(min);
        System.out.println(max);
        System.out.println("Tweets occur over" + ( (max-min) /  ((1000*60)) % 60) + "hours");

        // Keywords (logic.TermFrequency)
        featureActor featureActor = new featureActor();
        featureActor.getKeywords(tweetList);



        // Feature Vector (NumericTweetFeatures.makeFeatures)
        for(Tweet tweet : tweetList) {

            semanticActor semanticActor = new semanticActor(tweet);
            semanticActor.analyse(tweet.getText());
            System.out.println(tweet.getCreatedAtInt());
            System.out.println("offset:" + ((tweet.getCreatedAtInt()-min )/ 1000)  + " seconds");
            tweet.setOffset(((tweet.getCreatedAtInt()-min )/ 1000));
            FeatureVec(tweet);


        }
        PrintWriter out = null;

        // Export
        try {
            out = new PrintWriter(new FileWriter("../../0-data/processed/all_new.txt", true), true);
            System.out.println("Exported");
        } catch (IOException e) {
            e.printStackTrace();
        }


        for(Tweet tweet : tweetList) {
            if(tweet.getFeatureVector() != null){
                out.println(tweet.getFeatureVector());

            }

        }
        out.flush();
        out.close();
        System.out.println(featureVectorList);
        System.out.println("Finished...");

        //Outputs to browser
        long elapsedTime = System.currentTimeMillis() - startTime;
        long elapsedSeconds = elapsedTime / 1000;
        long elapsedMinutes = elapsedSeconds / 60;
        System.out.println("Time elapsed: " + elapsedMinutes + " minutes");
        return ok(Sanitise.toPrettyFormat(path));
    }




    public void FeatureVec(Tweet tweet){
        Map<String, Double> stringDoubleMap = NumericTweetFeatures.makeFeatures(tweet);
        //System.out.println(stringDoubleMap);
        tweet.setFeatures(stringDoubleMap);
        tweet.setFeatureVector(makeFeatureVector(stringDoubleMap));
        //System.out.println(tweet.getFeatureVector());

        if(tweet.getFeatureVector() != null){
            featureVectorList.add(tweet.getFeatureVector());

        }

    }






}

