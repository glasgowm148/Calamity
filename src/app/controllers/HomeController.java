package controllers;

// akka

import akka.actor.typed.ActorRef;
import akka.actor.typed.Scheduler;
import akka.actor.typed.javadsl.AskPattern;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
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
import play.mvc.Controller;
import play.mvc.Result;
import services.CounterActor;
import services.CounterActor.Command;
import services.CounterActor.GetValue;
import services.CounterActor.Increment;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
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

        //String jsonText = null;
        //jsonText = tweets.toString();
        //String prettyJson = toPrettyFormat(jsonText);
        //System.out.println("Pretty:\n" + prettyJson);

        File file = new File("/Users/pseudo/Documents/GitHub/HelpMe/src/conf/before_selection.json");
        ParseJSON(file);
        /*
        ObjectMapper mapper = new ObjectMapper();
        List<Tweet<String,Object>> list = new ArrayList<>();

        try (
                BufferedReader br = new BufferedReader(new FileReader(file))) {

            while (br.ready()) {
                String line = br.readLine();
                Tweet tweet = mapper.readValue(line, Tweet.class);

                clean(tweet);
                System.out.println("cleanedText:" + tweet.getText());
                System.out.println("cleanedText:" + tweet.getCreatedAt());

                System.out.println(".list.add(tweet);:");
                list.add(tweet);
                System.out.println(".list.add(tweet);");

            }
            System.out.println("for loop:");
            for (Tweet tweet : list) {
                System.out.println("~~~tweet :: " + tweet.getText());
            }



        } catch (IOException e) {
            e.printStackTrace();
            return internalServerError("IOException: Something went wrong");
        }
*/
        // Outputs to browser
        return ok("test");
    }

    /**
     * @param tweet
     * @return
     */
    public int analyse(String tweet) {

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Annotation annotation = pipeline.process(tweet);
        props.setProperty("ssplit.eolonly", "true");
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

    // Sentiment Analyser (./controllers/SentimentAnalyzer)
    public static void sentimentScore(Tweet tweet){
        SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer();
        sentimentAnalyzer.initialize();
        SentimentResult sentimentResult = sentimentAnalyzer.getSentimentResult(tweet.getText());

        // print results to console
        System.out.println("Sentiment Score: " + sentimentResult.getSentimentScore());
        System.out.println("Sentiment Type: " + sentimentResult.getSentimentType());
        System.out.println("Very positive: " + sentimentResult.getSentimentClass().getVeryPositive() + "%");
        System.out.println("Positive: " + sentimentResult.getSentimentClass().getPositive() + "%");
        System.out.println("Neutral: " + sentimentResult.getSentimentClass().getNeutral() + "%");
        System.out.println("Negative: " + sentimentResult.getSentimentClass().getNegative() + "%");
        System.out.println("Very negative: " + sentimentResult.getSentimentClass().getVeryNegative() + "%");
    }

    public static void clean(Tweet tweet){
        // Remove URLs, mentions, hashtags and whitespace
        tweet.setText(tweet.getText().trim()
                .replaceAll("http.*?[\\S]+", "")
                .replaceAll("@[\\S]+", "")
                .replaceAll("#", "")
                .replaceAll("[\\s]+", " "));

    }


        public static void ParseJSON(File file) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            try {
                CollectionType tweetListType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Tweet.class);
                List<Tweet> tweets = mapper.readValue(file, tweetListType);
                //tweets.forEach(System.out::println);
                for (Tweet tweet : tweets) {
                    System.out.println("~~~tweet :: " + tweet.getText());
                    clean(tweet);
                    System.out.println("~~~CleanedTweet :: " + tweet.getText());
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
}




        //public CompletionStage<Result> getLocation(String latitude, String longitude) {
    //    return ask(tweetActor, new tweetActor(latitude, longitude))
    //}


