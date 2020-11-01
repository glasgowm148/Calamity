package controllers;

// akka

import akka.actor.typed.ActorRef;
import akka.actor.typed.Scheduler;
import akka.actor.typed.javadsl.AskPattern;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import models.SentimentResult;
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

// Stanford CoreNLP (must be on classpath)
// Models
// Play
// Services


//import akka.actor.AbstractActor;
//import actors.TweetActor;


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

        String jsonText = null;
        File file = new File("/Users/pseudo/Documents/GitHub/HelpMe/src/conf/before_selection.json");
        try (
                FileInputStream is =new FileInputStream(file)
        ){
            final JsonNode json = Json.parse(is);
            jsonText = json.toString();
            System.out.println("Original Tweet:" + json.get("full_text"));

            String prettyJson = toPrettyFormat(jsonText);
            System.out.println("Pretty:\n" + prettyJson);
            // new mapper
            ObjectMapper mapper = new ObjectMapper();

            

            /// Create a new Tweet object
            Tweet tweets = mapper.readValue(json.toString(), Tweet.class);

            //Tweet tweets = mapper.readValue(new File("/Users/pseudo/Documents/GitHub/HelpMe/src/conf/alberta.json"), Tweet.class);
            System.out.println("##tweets" + tweets.getCreatedAt());

            // Remove URLs, mentions, hashtags and whitespace
            tweets.setText(tweets.getText().trim()
                    .replaceAll("http.*?[\\S]+", "")
                    .replaceAll("@[\\S]+", "")
                    .replaceAll("#", "")
                    .replaceAll("[\\s]+", " "));

            // Sentiment Analyser (./controllers/SentimentAnalyzer)
            SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer();
            sentimentAnalyzer.initialize();
            SentimentResult sentimentResult = sentimentAnalyzer.getSentimentResult(tweets.getText());

            // print results to console
            System.out.println("Sentiment Score: " + sentimentResult.getSentimentScore());
            System.out.println("Sentiment Type: " + sentimentResult.getSentimentType());
            System.out.println("Very positive: " + sentimentResult.getSentimentClass().getVeryPositive()+"%");
            System.out.println("Positive: " + sentimentResult.getSentimentClass().getPositive()+"%");
            System.out.println("Neutral: " + sentimentResult.getSentimentClass().getNeutral()+"%");
            System.out.println("Negative: " + sentimentResult.getSentimentClass().getNegative()+"%");
            System.out.println("Very negative: " + sentimentResult.getSentimentClass().getVeryNegative()+"%");

            // Outputs to browser
            return ok(prettyJson);

        } catch(IOException e){
            return internalServerError("Something went wrong");
        }






    }

    /**
     *
     * @param tweet
     * @return
     */
    public int analyse(String tweet) {

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Annotation annotation = pipeline.process(tweet);
        props.setProperty("ssplit.eolonly","true");
        //props.setProperty("parse.binaryTrees","true");
        pipeline.annotate(annotation);
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            System.out.println("---");
            System.out.println(sentence.get(CoreAnnotations.TextAnnotation.class));
            System.out.println(sentence.get(CoreAnnotations.TextAnnotation.class));
            System.out.println(sentence.get(SentimentCoreAnnotations.SentimentClass.class));
        }
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            return RNNCoreAnnotations.getPredictedClass(tree);
        }
        return 0;
    }

    public static String toPrettyFormat(String jsonString) {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(jsonString).getAsJsonObject();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(json);

        return prettyJson;
    }







    //public CompletionStage<Result> getLocation(String latitude, String longitude) {
    //    return ask(tweetActor, new tweetActor(latitude, longitude))
    //}

}
