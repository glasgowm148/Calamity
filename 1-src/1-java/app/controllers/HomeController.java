package controllers;

import java.io.*;
import java.time.Duration;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
// Akka
import akka.actor.typed.ActorRef;
import akka.actor.typed.Scheduler;
import akka.actor.typed.javadsl.AskPattern;

// Play
import play.mvc.Controller;
import play.mvc.Result;

// Project imports
import actors.*;
import logic.*;

// Template imports
import services.CounterActor.Command;
import services.CounterActor.GetValue;
import services.CounterActor.Increment;


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

    private final ActorRef<Command> counterActor; // , TweetActor
    private final ActorRef sentimentActor;

    private final Scheduler scheduler;

    private final Duration askTimeout = Duration.ofSeconds(3L);





    @Inject
    public HomeController(ActorRef<Command> counterActor, ActorRef sentimentActor, Scheduler scheduler) {
        //TweetActor = system.actorOf(tweetActor.props());
        this.counterActor = counterActor;
        this.sentimentActor = sentimentActor;
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

        // Start timer
        long startTime = System.currentTimeMillis();

        // Parse into Tweet.class
        jsonReader reader = new jsonReader();
        reader.parse();

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




}

