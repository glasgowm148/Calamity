package controllers;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import Utils.infoRepository;
import Utils.inputOutput;
import akka.actor.*;

// Play
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.chen0040.embeddings.GloVeModel;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import features.NumericTweetFeatures;
import models.Tweet;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

// Project imports
import actors.*;
import logic.*;
import scala.compat.java8.FutureConverters;
import twitter.twittertext.Extractor;
import twitter.twittertext.TwitterTextParseResults;
import twitter.twittertext.TwitterTextParser;
import twitter4j.TwitterException;
//import views.html.resultPage;


import static Utils.inputOutput.convertFloatsToDoubles;

import static controllers.HomeController.StaticPath.output_file;
import static controllers.HomeController.StaticPath.tweetList;
import static features.NumericTweetFeatures.makeFeatureVector;


public class HomeController extends Controller {

    public static class StaticPath {
        public static String path = "lib/2020A_tweets/selected/run";
        public static String output_file = "cluster_run2";
        public static List<Tweet> tweetList  = new ArrayList<>();
        //System.out.println("New parseEvent: \n" + path);
    }


    // Start timer for tracking efficiency
    long startTime = System.currentTimeMillis();

    /**
    // Define Actor References
    private final ActorRef event_actor;
    private final ActorRef sentiment_actor;

    // Initialise the Actor references using dependency injection
    //@Inject
    public HomeController(ActorSystem actorObj) {
        event_actor = actorObj.actorOf(eventActor.getProps());
        sentiment_actor = actorObj.actorOf(eventActor.getProps());
    }
    **/

    // index() is triggered on GET to localhost:9000/
    public Result index() throws IOException {


        // Get jsonl files
        try (Stream<Path> paths = Files.walk(Paths.get(StaticPath.path),2)) {
            paths.map(Path::toString).filter(f -> f.endsWith(".jsonl"))
                    .forEach(this::parseEvent);
        } catch (Exception e) { e.printStackTrace(); }



        printTimer(startTime);
        return ok(new String(Files.readAllBytes(Paths.get("../../0-data/processed/" + output_file + ".txt"))));
       // return ok(inputOutput.VectorToPrettyFormat(new File(output_file)));         //return ok(Sanitise.toPrettyFormat(new File("../../1-src/1-java/conf/10.jsonl")));
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
     * @param path - the event path
     * Parses each event into Tweet.class
     * Calculates the offset and event keywords before passing to tweetAnalyser()
     * @return
     */
    public void parseEvent(String path)  {
        GloVeModel model = new GloVeModel();
        model.load("lib/glove", 200);
        //Properties props = new Properties();
        //props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment"); // ner, entitymentions
        //props.setProperty("parse.binaryTrees", "true");
        //StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // is = a FileInputStream of the path
        try (InputStream is = new FileInputStream(String.valueOf(path))) {

            // @lines = A Stream of strings where each line represents one tweet in the event
            try (Stream<String> lines = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)).lines()) {

                // Instantiate a new ObjectMapper object
                ObjectMapper mapper = new ObjectMapper();

                // Configure the mapper to accept single values as arrays. - This is required so we can deserialise each line into an array
                mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

                for (String s : (Iterable<String>) lines::iterator) {    // Iterate through each line

                    try {

                        // @n = A JsonNode of the Tweet
                        JsonNode n = Json.parse(s);

                        // @tweet = JsonNode mapped to the Tweet model using JacksonXMLs 'treeToValue'
                        Tweet tweet = new Tweet();
                        tweet = mapper.treeToValue(n, Tweet.class); // here


                        final Extractor extractor = new Extractor();
                        List<String> hashtags = extractor.extractHashtags(tweet.getText());
                        tweet.setHashtags(hashtags);

                        // Tokenizer
                        List<String> tokens = Twokenize.tokenize(tweet.getText());
                        String[] str_array = tokens.toArray(new String[0]);
                        tweet.setTokens(str_array);


                        // Text features using Twitter-Text
                        final TwitterTextParseResults result = TwitterTextParser.parseTweet(tweet.getText());
                        tweet.setWeightedLength(result.weightedLength);
                        tweet.setPermillage(result.permillage);

                        // Pre-Process
                        Sanitise.clean(tweet);

                        //Annotation annotation = pipeline.process(tweet.getText());
                        //pipeline.annotate(annotation);
                        //for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                        //    Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                        //    tweet.setVectorTree(RNNCoreAnnotations.getNodeVector(tree)); //RNNCoreAnnotations.getPredictedClass(tree);
                        //}


                        // wordEmbeddings
                        float[] d = model.encodeDocument(tweet.getText());
                        tweet.setDimensions(d);


                        // Progress 'bar'
                        System.out.print(".");
                        // make the features
                        Map<String, Double> stringDoubleMap = NumericTweetFeatures.makeFeatures(tweet);

                        //System.out.println(stringDoubleMap);

                        tweet.setFeatures(stringDoubleMap);
                        tweet.setFeatureVector(makeFeatureVector(stringDoubleMap));

                        // @tweetList = An ArrayList we add all the to.
                        tweetList.add(tweet);

                        //if(tweet.getFeatureVector() != null){
                        //    featureVectorList.add(tweet.getFeatureVector());
                        //}


                    } catch (JsonProcessingException jsonProcessingException) { jsonProcessingException.printStackTrace(); }

                }

                System.out.println("\n" + tweetList.size() + " tweets read into model from " + path);
                int min = getMin();
                for(Tweet tweet: tweetList){
                    // Time offset

                    tweet.setOffset(((tweet.getCreatedAtInt() - min)));
                }

                // Instantiate a new featureActor()
                featureActor featureActor = new featureActor();

                // getKeywords gets the TFIDF
                featureActor.getKeywords(tweetList);

                inputOutput.printVector(HomeController.StaticPath.output_file, tweetList);

            }


        } catch (IOException e) {
            System.out.println(e.toString());


        }


    }



    // Get IntSummaryStatistics to calculate the offset
    public static int getMin() {
        // Offset
        IntSummaryStatistics summaryStatistics = tweetList.stream()
                .map(Tweet::getCreatedAtStr)
                .mapToInt(Integer::parseInt)
                .summaryStatistics();

        int max = summaryStatistics.getMax();
        int min = summaryStatistics.getMin();
        System.out.println("Tweets occur over a span of " + ( (max-min) /  ((1000*60)) % 60) + " hours");
        System.out.print("Parsing");
        return min;
    }


    public static void printVector(String file) {
        PrintWriter out = null;

        // Export
        try {
            out = new PrintWriter(new FileWriter("../../0-data/processed/" + file + ".txt", true), true);

        } catch (IOException e) {
            e.printStackTrace();
        }



        for (Tweet tweet : tweetList) {
            double[] d = convertFloatsToDoubles(tweet.getDimensions());
            if (tweet.getFeatureVector() != null) {
                assert out != null;

                // Print the feature vector
                out.print(tweet.getFeatureVector());


                // Add the BERT Word Embeddings
                out.print(",[");
                for(double x : d){ out.print(x + ", "); }
                out.print("]");


            }
            assert out != null;
            out.println("");

        }
        assert out != null;
        out.flush();
        out.close();
        System.out.println("Exported to" + file + ".txt");
    }



    /*
    public CompletionStage<Result> resultEvent(String name) throws TwitterException, ExecutionException, InterruptedException {
        return FutureConverters.toJava(ask(event_actor, new eventActor.parse("/test.jsonl"), 5000))
                .thenApplyAsync(userInfo -> ok(resultPage.render((infoRepository) userInfo)));
    }

     */



}

