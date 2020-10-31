package controllers;

//import actors.TweetActor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Scheduler;
import akka.actor.typed.javadsl.AskPattern;
import com.fasterxml.jackson.databind.JsonNode;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import models.Tweet;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.CounterActor;
import services.CounterActor.Command;
import services.CounterActor.GetValue;
import services.CounterActor.Increment;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.CompletionStage;

//package me.aboullaite.corenlp.sentimentanalysis.services;


//import akka.actor.AbstractActor;


/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
    private final ActorRef<Command> counterActor; // , TweetActor
    private final Scheduler scheduler;
    private final Duration askTimeout = Duration.ofSeconds(3L);

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


    private Result renderIndex(Integer hitCounter) {
        File file = new File("/Users/pseudo/Documents/GitHub/HelpMe/src/conf/alberta.json");
        try (
                FileInputStream is =new FileInputStream(file)
        ){
            final JsonNode json = Json.parse(is);
            Tweet tweet = new Tweet(json);
            //ObjectMapper objectMapper = new ObjectMapper();
            //JsonNode jsonNode = objectMapper.readTree(json);

            //System.out.println(cleanTweets(tweet.getText()));

            //Status tweet_status = new Tweet(status.getCreatedAt(), status.getId(), status.getText(), null, status.getUser().getName(), status.getUser().getScreenName(), status.getUser().getProfileImageURL());
            // Clean up tweets
            String text = tweet.getText().trim()
                    // remove links
                    .replaceAll("http.*?[\\S]+", "")
                    // remove usernames
                    .replaceAll("@[\\S]+", "")
                    // replace hashtags by just words
                    .replaceAll("#", "")
                    // correct all multiple white spaces to a single white space
                    .replaceAll("[\\s]+", " ");

            // Tweet.setText(text);
            //Tweet.setSentimentType(analyzerService.analyse(text));
            System.out.println(json.get("full_text"));
            System.out.println(tweet.getText());
            System.out.println("scores from 0 to 4 based on whether the analysis comes back with Very Negative, Negative, Neutral, Positive or Very Positive respectively.");
            System.out.println("score" + analyse(text));


            return ok(text);
        } catch(IOException e){
            return internalServerError("Something went wrong");
        }

    }



    public int analyse(String tweet) {

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Annotation annotation = pipeline.process(tweet);
        //props.setProperty("ssplit.eolonly","true");
        props.setProperty("parse.binaryTrees","true");
        pipeline.annotate(annotation);
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            System.out.println("---");
            System.out.println(sentence.get(CoreAnnotations.TextAnnotation.class));
            System.out.println(sentence.get(SentimentCoreAnnotations.SentimentClass.class));
        }
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            return RNNCoreAnnotations.getPredictedClass(tree);
        }
        return 0;
    }



    //public CompletionStage<Result> getLocation(String latitude, String longitude) {
    //    return ask(tweetActor, new tweetActor(latitude, longitude))
    //}

}
