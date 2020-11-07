package controllers;


// akka

import NLPAlgorithms.DocumentBOW;
import actors.Twokenize;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Scheduler;
import akka.actor.typed.javadsl.AskPattern;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import org.apache.commons.lang3.builder.ToStringBuilder;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletionStage;

import static pipelines.TrueCase.TrueCase;


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

        File file = new File("/Users/pseudo/Documents/GitHub/HelpMe/src/conf/before_selection.json");

        String prettyJson = toPrettyFormat(file);

        ParseJSON(file);
        /*
            models.Tweet@760f4e6c[mapper=com.fasterxml.jackson.databind.ObjectMapper@49ca350e,jsonText=<null>,

            createdAt=Fri Jul 19 17:08:23 +0000 2019,id=1152263930645024768,

            idStr=1152263930645024768,

            text=Athens residents flee as strong earthquake shakes Greek capital A strong earthquake has struck near Athens,

            causing residents of the Greek capital to run into the streets. The city’s Institute of Geodynamics gave… ,

            truncated=false,entities=models.Entities@1f0a690d,user=models.User@7f04da7,isQuoteStatus=false,

            retweetCount=0,
            favoriteCount=0,
            favorited=false,
            retweeted=false,
            data=<null>,
            retweet=<null>,
            properties=<null>]

         */
        // Outputs to browser
        return ok(prettyJson);
    }

    public void ParseJSON(File file) {
        // https://stackoverflow.com/questions/54947356/how-can-i-iterate-through-json-objects-using-jackson
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true); // https://stackoverflow.com/questions/20837856/can-not-deserialize-instance-of-java-util-arraylist-out-of-start-object-token
        try {
            CollectionType tweetListType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Tweet.class);
            List<Tweet> tweets = mapper.readValue(file, tweetListType);
            //tweets.forEach(System.out::println);

            for (Tweet tweet : tweets) {
                // System.out.println("~~~tweet :: " + tweet.getText());
                clean(tweet);
                System.out.println("~~~CleanedTweet :: " + tweet.getText());
                System.out.println(tweet.getText());
                System.out.println(ToStringBuilder.reflectionToString(tweet)); // https://stackoverflow.com/questions/31847080/how-to-convert-any-object-to-string
                //sentimentScore(tweet);
                // twitterStatus.setSentimentType(analyzerService.analyse(text));
                // monolingual slang dict
                // https://github.com/ghpaetzold/questplusplus/blob/master/src/shef/mt/tools/mqm/resources/SlangDictionary.java
                //analyse(tweet.getText());
                System.out.println("tokenize():" + Twokenize.tokenizeRawTweetText(tweet.getText()));
                TrueCase(tweet.getText());
                //BagOfWords(ArrayList<String> dir, ArrayList<String> lex, String sPath
                //BagOfWords nlp = new NLPAlgorithms.BagOfWords(directories, lex, stopWords);
                DocumentBOW doc = new DocumentBOW();
                System.out.println(doc.makeDocumentBOW(tweet.getText()));


            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public int analyse(String tweet) {
        // https://aboullaite.me/stanford-corenlp-java/

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment"); // ner, entitymentions
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Annotation annotation = pipeline.process(tweet);
        //props.setProperty("ssplit.eolonly", "true");
        props.setProperty("parse.binaryTrees","true");
        pipeline.annotate(annotation);
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            System.out.println("---");
            // word
            System.out.println(sentence.get(CoreAnnotations.TextAnnotation.class));
            System.out.println(sentence.get(SentimentCoreAnnotations.SentimentClass.class));

            String word = sentence.get(CoreAnnotations.TextAnnotation.class);
            String lemma = sentence.get(CoreAnnotations.LemmaAnnotation.class);
            String pos = sentence.get(CoreAnnotations.PartOfSpeechAnnotation.class);
            String ne = sentence.get(CoreAnnotations.NamedEntityTagAnnotation.class);
            String normalized = sentence.get(CoreAnnotations.NormalizedNamedEntityTagAnnotation.class);
            System.out.println("word:" + word + "lemma: " + lemma + "pos: " + pos + "ne: " + ne + "normalised " + normalized);
        }
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            return RNNCoreAnnotations.getPredictedClass(tree);
        }
        return 0;
    }

    public static String toPrettyFormat(File file) {
       String jsonText = null;
        try (
                FileInputStream is = new FileInputStream(file)
        ) {
            final JsonNode json = Json.parse(is);
            JsonParser parser = new JsonParser();
            //JsonObject json = parser.parse(jsonText).getAsJsonObject();
            Tweet tweets = new Tweet(json);
            jsonText = tweets.toString();
            

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String prettyJson = gson.toJson(json);

            return prettyJson;
        } catch (JsonProcessingException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonText;
    }

    public static void sentimentScore(Tweet tweet){
        // https://github.com/Ruthwik/Sentiment-Analysis
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
                .replaceAll("[\\s]+", " ")
                //replace text between {},[],() including them
                .replaceAll("\\{.*?\\}", "")
                .replaceAll("\\[.*?\\]", "")
                .replaceAll("\\(.*?\\)", "")
                .replaceAll("[^A-Za-z0-9(),!?@\'\\`\"\\_\n]", " ")
                .replaceAll("[/]"," ")
                .replaceAll(";"," "));
        /*
        Pattern charsPunctuationPattern = Pattern.compile("[\\d:,\"\'\\`\\_\\|?!\n\r@;]+");
        String input_text = charsPunctuationPattern.matcher(tweet.getText().trim().toLowerCase()).replaceAll("");
        //Collect all tokens into labels collection.    
        Collection<String> labels = Arrays.asList(input_text.split(" ")).parallelStream().filter(label->label.length()>0).collect(Collectors.toList());
        //get from standard text files available for Stopwords. e.g https://algs4.cs.princeton.edu/35applications/stopwords.txt
        labels = labels.parallelStream().filter(label ->  !StopWords.getStopWords().contains(label.trim())).collect(Collectors.toList());
        */

    }

    
  




}




        //public CompletionStage<Result> getLocation(String latitude, String longitude) {
    //    return ask(tweetActor, new tweetActor(latitude, longitude))
    //}


