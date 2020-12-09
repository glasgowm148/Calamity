package controllers;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Scheduler;
import akka.actor.typed.javadsl.AskPattern;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import logic.*;
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
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONObject;
import tweetfeatures.NumericTweetFeatures;

import static logic.TermFrequency.getKeywords;
import static tweetfeatures.NumericTweetFeatures.makeFeatureVector;

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

    String UTF8 = "UTF8";
    int BUFFER_SIZE = 8192;
    private final ActorRef<Command> counterActor; // , TweetActor

    private final Scheduler scheduler;

    private final Duration askTimeout = Duration.ofSeconds(3L);

    //private final File path = new File("../../0-data/raw/data/2020/2020-A/tweets/whaley_bridge_collapse/selected.jsonl");
    private final File path = new File("/Users/mark/Documents/GitHub/HelpMe/1-src/1-java/conf/10.jsonl");

    List<Tweet> tweetList = new ArrayList<>();
    List<Vector> featureVectorList = new ArrayList<>();
    private Object[] objArray;

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

        //  sbt -java-home /Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home

        /** Get all .json files within a directory **/
        List<Path> bList = collectFiles();
        tweetList = ParseJsonList(bList);
        /** Parse into Tweet.class **/
        //tweetList = ParseJSON();
        System.out.println("\ntweetList.size():\n" + tweetList.size());



        /** Keywords (logic.TermFrequency) **/
        getKeywords(tweetList);

        /** 51774 Tweets **/
        System.out.println("\ntweetList.size():\n" + tweetList.size());

        /** Feature Vector (NumericTweetFeatures.makeFeatures) **/
        for(Tweet tweet : tweetList) {
            Sentiment(tweet);

            // Create the feature vector
            FeatureVec(tweet);

        }
        PrintWriter out = null;

        /** Export **/
        try {
            out = new PrintWriter(new FileWriter("../../0-data/processed/all_new.txt"));
            System.out.print("Exported");
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
        return ok(Sanitise.toPrettyFormat(path));
    }

    private List<Path> collectFiles() {
        List<Path> bList = null;
        try {
            bList = Files.find(Paths.get("../../0-data/raw/data/2020/2020-A/tweets/"),
                    999,
                    (p, bfa) -> bfa.isRegularFile()
                    && p.getFileName().toString().matches(".*\\.jsonl"))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(bList);
        return bList;
    }

    public List<Tweet> ParseJSON()  {
        try (InputStream is = new FileInputStream(path)) {
            try (Stream<String> lines = new BufferedReader(new InputStreamReader(is, UTF8)).lines()) {
                return parseOne(lines);
            }


        } catch (IOException e) {
            System.out.println(e.toString());
        }



        return null;
    }
    public List<Tweet> ParseJsonList(List<Path> bList)  {
        for (Path l : bList){
            try (InputStream is = new FileInputStream(String.valueOf(l))) {
                try (Stream<String> lines = new BufferedReader(new InputStreamReader(is)).lines()) {

                    return parseOne(lines);

                }


            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }

        return null;
    }

    private void Sentiment(Tweet tweet) {
        /**
         * Sentiment Analysis
         */
        // SentimentScore
        // https://github.com/Ruthwik/Sentiment-Analysis
        SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer();
        sentimentAnalyzer.initialize();
        SentimentResult sentimentResult = sentimentAnalyzer.getSentimentResult(tweet.getText());
        tweet.setPositiveSentiment(sentimentResult.getSentimentClass().getPositive());
        tweet.setNegativeSentiment(sentimentResult.getSentimentClass().getNegative());

        // print results to console
        /*
        System.out.println("Sentiment Score: " + sentimentResult.getSentimentScore());
        System.out.println("Sentiment Type: " + sentimentResult.getSentimentType());
        System.out.println("Very positive: " + sentimentResult.getSentimentClass().getVeryPositive());
        System.out.println("Positive: " + sentimentResult.getSentimentClass().getPositive());
        System.out.println("Neutral: " + sentimentResult.getSentimentClass().getNeutral());
        System.out.println("Negative: " + sentimentResult.getSentimentClass().getNegative());
        System.out.println("Very negative: " + sentimentResult.getSentimentClass().getVeryNegative());
        tweet.setSentiment(sentimentResult);
        */


        //System.out.println("\n###sentiment:" + tweet.getSentimentScore());
        //twitterStatus.setSentimentType(analyzerService.analyse(text));

        // Slang
        // https://github.com/ghpaetzold/questplusplus/blob/master/src/shef/mt/tools/mqm/resources/SlangDictionary.java

        // Analyse()
        // System.out.println("\nanalyse():\n returns the distributed representation of the node \n" + analyse(tweet.getText()));
        // returns the distributed representation of the node, which is a vector.
        // This corresponds to the vectors a, b, c, p1, and p2 in Section 4 of the paper about the work: http://nlp.stanford.edu/pubs/SocherEtAl_EMNLP2013.pdf . It is not easily human interpretable, but a function of it predicts the node's sentiment, as explained in the paper.
        // analyse(tweet.getText());

        // Dependency Graph +
        // BasicPipeline(tweet.getText());
    }


    public Serializable analyse(String tweet) {
        // https://aboullaite.me/stanford-corenlp-java/

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment"); // ner, entitymentions
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Annotation annotation = pipeline.process(tweet);
        //props.setProperty("ssplit.eolonly", "true");
        props.setProperty("parse.binaryTrees","true");
        pipeline.annotate(annotation);
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            // word
            //System.out.println(sentence.get(CoreAnnotations.TextAnnotation.class));
            //System.out.println(sentence.get(SentimentCoreAnnotations.SentimentClass.class));

            String word = sentence.get(CoreAnnotations.TextAnnotation.class);
            String lemma = sentence.get(CoreAnnotations.LemmaAnnotation.class);
            String pos = sentence.get(CoreAnnotations.PartOfSpeechAnnotation.class);
            String ne = sentence.get(CoreAnnotations.NamedEntityTagAnnotation.class);
            String normalized = sentence.get(CoreAnnotations.NormalizedNamedEntityTagAnnotation.class);
            //System.out.println("word:" + word + "lemma: " + lemma + "pos: " + pos + "ne: " + ne + "normalised " + normalized);
        }
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            return RNNCoreAnnotations.getNodeVector(tree); //RNNCoreAnnotations.getPredictedClass(tree);

        }
        return 0;
    }

    public void printArray(Object[] objArray) {
        System.out.println("Elements in Array :");
        for (Object o : objArray) {
            System.out.println(o);

        }


    }

    public List<Tweet> parseOne(Stream<String> lines) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);


        for (String s : (Iterable<String>) lines::iterator) {
            // Parses a string as JSON
            try {
                JsonNode n = Json.parse(s);
                Tweet tweet = mapper.treeToValue(n, Tweet.class); // here
                tweetList.add(tweet); // Sanitise.clean(tweet);
            } catch (JsonProcessingException jsonProcessingException) {
                jsonProcessingException.printStackTrace();
            }

        }

        return tweetList;
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

