package controllers;

import Utils.APIUtils;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Scheduler;
import akka.actor.typed.javadsl.AskPattern;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

import org.json.JSONObject;
import tweetfeatures.NumericTweetFeatures;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static tweetfeatures.NumericTweetFeatures.makeFeatureVector;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 *
 * I've used an Akka-play seed,
 * the Akka cluster functionality is ignored/bypassed for now
 *
 * renderIndex() calls ParseJSON() where the processing happens
 *
 * Play Framework - Using Scala/SBT
 *
 * !! To recompile, navigate to browser and refresh localhost:9000
 *
 */

public class HomeController extends Controller {
    private final ActorRef<Command> counterActor; // , TweetActor
    private final Scheduler scheduler;
    private final Duration askTimeout = Duration.ofSeconds(3L);
    Object[] objArray;
    List<Tweet> tweetList = new ArrayList<>();

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

        File file = new File("/Users/pseudo/Documents/GitHub/HelpMe/src/conf/before_selection.json");

        String prettyJson = Sanitise.toPrettyFormat(file);
        ParseJSON();

        // Outputs to browser
        return ok(prettyJson);
    }



    public void ParseJSON()  {
        System.out.println("Parsing....");

        /**
         * File is in NDJson format (One JSON object per line)
         * Reading it in with Iterable<String> currently
         */


        try (InputStream is = new FileInputStream(new File("conf/alberta.json"))) {

            try (Stream<String> lines = new BufferedReader(new InputStreamReader(is)).lines()) {

                // Uses String iterator - parses 151/500 into Tweet.class
                tweetList = parseOne(lines);

                // Uses NDJson.java - parses all tweets into Object[]
                // parseTwo(is);
                for(Tweet tweet : tweetList) {
                    Features(tweet);
                }
                //Sentiment();

                }





        } catch (IOException e) {
            //SOPs
            System.out.println(e.toString());
        }

    }

    private void Sentiment() {
        /**
         * Sentiment Analysis
         */
        // SentimentScore
        //sentimentScore(tweet);
        //System.out.println("\n###sentiment:" + tweet.getSentimentScore());

        //twitterStatus.setSentimentType(analyzerService.analyse(text));

        // Slang
        // https://github.com/ghpaetzold/questplusplus/blob/master/src/shef/mt/tools/mqm/resources/SlangDictionary.java

        // System.out.println("\nanalyse():\n returns the distributed representation of the node \n" + analyse(tweet.getText()));
        // returns the distributed representation of the node, which is a vector.
        // This corresponds to the vectors a, b, c, p1, and p2 in Section 4 of the paper about the work: http://nlp.stanford.edu/pubs/SocherEtAl_EMNLP2013.pdf . It is not easily human interpretable, but a function of it predicts the node's sentiment, as explained in the paper.
        //analyse(tweet.getText());

        // Dependency Graph +
        //BasicPipeline(tweet.getText());
    }

    public Tweet Sanitise(Tweet tweet){

        /**
         * Sanitisation
         * URLs / Hashtags / Stopwords / Tokenise
         */

        // Clean
        Sanitise.clean(tweet);
        // Stopwords
        tweet.setText(Arrays.toString(Sanitise.removeStopWords(tweet.getText())));

        // Tokenise
        tweet.setText(String.valueOf(Twokenize.tokenizeRawTweetText(tweet.getText())));

        //System.out.println("\n### Cleaned Text ###\n:" + tweet.getText());

        return tweet;
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
            System.out.println(sentence.get(CoreAnnotations.TextAnnotation.class));
            System.out.println(sentence.get(SentimentCoreAnnotations.SentimentClass.class));

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

    public void sentimentScore(Tweet tweet){
        // https://github.com/Ruthwik/Sentiment-Analysis
        SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer();
        sentimentAnalyzer.initialize();
        SentimentResult sentimentResult = sentimentAnalyzer.getSentimentResult(tweet.getText());

        // print results to console
        System.out.println("Sentiment Score: " + sentimentResult.getSentimentScore());
        System.out.println("Sentiment Type: " + sentimentResult.getSentimentType());
        System.out.println("Very positive: " + sentimentResult.getSentimentClass().getVeryPositive());
        System.out.println("Positive: " + sentimentResult.getSentimentClass().getPositive());
        System.out.println("Neutral: " + sentimentResult.getSentimentClass().getNeutral());
        System.out.println("Negative: " + sentimentResult.getSentimentClass().getNegative());
        System.out.println("Very negative: " + sentimentResult.getSentimentClass().getVeryNegative());
        tweet.setSentiment(sentimentResult);

    }

    public void printArray(Object[] objArray) {
        System.out.println("Elements in Array :");
        for (Object o : objArray) {
            System.out.println(o);

        }


    }

    public List<Tweet> parseOne(Stream<String> lines) {
        ObjectMapper mapper = new ObjectMapper();


        for (String s : (Iterable<String>) lines::iterator) {

            // Parses a string as JSON
            try {
                JsonNode n = Json.parse(s);
                Tweet tweet = mapper.treeToValue(n, Tweet.class);
                System.out.println("\nText:\n" + tweet.getText());
                tweetList.add(Sanitise(tweet));
            } catch (JsonProcessingException jsonProcessingException) {

                //jsonProcessingException.printStackTrace();
            }

            System.out.println("Tweets imported into Tweet.class model:");
            System.out.println(tweetList.size());


        }

        return tweetList;
    }

    public void parseTwo(InputStream is){
            List<JSONObject> tweetArray = new ArrayList<>();


            tweetArray = NDJson.parse(is);

            // toArray() returns an array containing all of the elements in this list in the correct order
            objArray = tweetArray.toArray();

            // printArray(objArray);
            System.out.println("Tweets imported into Object Array:");
            System.out.println(objArray.length);

    }

    public void Features(Tweet tweet){
        /**
         * Feature Vector
         *
         * Not 100% what this should look like,
         *  1. one tuple per tweet in the set? [(x,y), (x,y), (x,y)]
         *  2. one set per tweet?  [[(x,y), (x,y), (x,y)],[(x,y), (x,y), (x,y)]]
        */

        /**
         * Feature Extraction
         */

        //DocumentLex doc = null;

        //System.out.println("\ndoc.makeDocumentLex(tweet.getText():");
        //System.out.println("\nDoc text:\n" + tweet.getText());
        //System.out.println(doc.makeDocumentLex(tweet.getText()));
        Map<String, Double> stringDoubleMap = NumericTweetFeatures.makeFeatures(tweet);

        System.out.println("\nNumericTweetFeatures.makeFeatures(tweet):");
        System.out.println(stringDoubleMap);

        makeFeatureVector(stringDoubleMap);

        // Term Frequency
         //System.out.println("\nTerm Frequency:");
         //System.out.println(TermFrequency.getTF(tweet.getText()));

         // FeatureVector.java
         //List<String> topics = new ArrayList<String>();
         //List<String> places = new ArrayList<String>();
         //System.out.println(ToStringBuilder.reflectionToString((new FeatureVector(topics, places, tweet.getText()))));

         // Tweet2Vec.java
         // System.out.println("\nTweet2VEC");
         // new Tweet2vecModel(tweetsList);

         // TFIDFCalculator (Running on dummy-text)
         //List<String> doc1 = Arrays.asList("Lorem", "ipsum", "dolor", "ipsum", "sit", "ipsum");
         //List<String> doc2 = Arrays.asList("Vituperata", "incorrupte", "at", "ipsum", "pro", "quo");
         //List<String> doc3 = Arrays.asList("Has", "persius", "disputationi", "id", "simul");
         //List<List<String>> documents = Arrays.asList(doc1, doc2, doc3);

         //TFIDFCalculator calculator = new TFIDFCalculator();
         //double tfidf = calculator.tfIdf(doc1, documents, "ipsum");
         //System.out.println("TF-IDF (ipsum) = " + tfidf);





    }


}




        //public CompletionStage<Result> getLocation(String latitude, String longitude) {
    //    return ask(tweetActor, new tweetActor(latitude, longitude))
    //}


