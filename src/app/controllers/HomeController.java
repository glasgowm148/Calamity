package controllers;

//import actors.TweetActor;

import akka.actor.typed.ActorRef;
//import akka.actor.AbstractActor;
import akka.actor.ActorSystem;
import akka.actor.typed.Scheduler;
import akka.actor.typed.javadsl.Adapter;
import akka.actor.typed.javadsl.AskPattern;
import akka.cluster.typed.Cluster;
import akka.cluster.typed.ClusterSingleton;
import akka.cluster.typed.SingletonActor;
import play.mvc.Controller;
import play.mvc.Result;
import services.CounterActor;
import services.CounterActor.Command;
import services.CounterActor.GetValue;
import services.CounterActor.Increment;

import javax.inject.Inject;
import java.time.Duration;
import java.util.concurrent.CompletionStage;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import javax.inject.Inject;
import com.fasterxml.jackson.databind.JsonNode;
import play.Environment;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {


    private ActorRef<Command> counterActor; // , TweetActor
    private Scheduler scheduler;

    private Duration askTimeout = Duration.ofSeconds(3L);
    // public ImportScheduler(final ActorSystem actorSystem, #Named("user_import_actor") ActorRef UserImportActor) {
    
    private final Environment env;


    @Inject
    public HomeController(ActorRef<CounterActor.Command> counterActor, Scheduler scheduler, final Environment env) {
        //TweetActor = system.actorOf(tweetActor.props());
        this.counterActor = counterActor;
        this.scheduler = scheduler;
        this.env = env;
    }


    public CompletionStage<Result> index() {
        // https://www.playframework.com/documentation/2.8.x/AkkaTyped#Using-the-AskPattern-&-Typed-Scheduler
        return AskPattern.<Command, Integer>ask(
                counterActor,
                GetValue::new,
                askTimeout,
                scheduler)
                .thenApply(this::renderIndex);
    }

    public CompletionStage<Result> increment() {
        // https://www.playframework.com/documentation/2.8.x/AkkaTyped#Using-the-AskPattern-&-Typed-Scheduler
        return AskPattern.<Command, Integer>ask(
                counterActor,
                Increment::new,
                askTimeout,
                scheduler)
                .thenApply(this::renderIndex);
    }

    private Result renderIndex(Integer hitCounter) {
        File file = new File("/Users/pseudo/Documents/GitHub/HelpMe/src/conf/alberta.json");

        try (
                FileInputStream is =new FileInputStream(file);
        ){
            final JsonNode json = Json.parse(is);
            return ok(json);
        } catch(IOException e){
            return internalServerError("Something went wrong");
        }
    
    }

   

    //public CompletionStage<Result> getLocation(String latitude, String longitude) {
    //    return ask(tweetActor, new tweetActor(latitude, longitude))
    //}

}
