package controllers;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Scheduler;
import akka.actor.typed.javadsl.AskPattern;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.logging.RedwoodConfiguration;
import logic.Sanitise;
import logic.SentimentAnalyzer;
import logic.TFIDFCalculator;
import logic.Twokenize;
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
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;
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


    private Result renderIndex(Integer hitCounter)  {

        File file = new File("/Users/pseudo/Documents/GitHub/HelpMe/src/conf/before_selection.json");

        String prettyJson = Sanitise.toPrettyFormat(file);
        ParseJSON(file);

        // Outputs to browser
        return ok(prettyJson);
    }

    public void ParseJSON(File file) {
        System.out.println("Parsing....");
        RedwoodConfiguration.current().clear().apply();

        try (InputStream is = new FileInputStream(new File("/Users/pseudo/Documents/GitHub/HelpMe/src/conf/testJSON.json"))) {
            try (Stream<String> lines = new BufferedReader(new InputStreamReader(is)).lines()) {

                ObjectMapper mapper = new ObjectMapper();
                for (String s : (Iterable<String>) lines::iterator) {

                    //System.out.println("\nJsonString\n");
                    //System.out.println(s);

                    JsonNode n = Json.parse(s);
                    //System.out.println(n);

                    Tweet tweet = mapper.treeToValue(n, Tweet.class);

                    //System.out.println("\nTweet Text:\n" + tweet.getText());
                    Sanitise.clean(tweet);
                    //System.out.println("\nCleanedTweet:\n" + tweet.getText());

                    //System.out.println(ToStringBuilder.reflectionToString(tweet)); // https://stackoverflow.com/questions/31847080/how-to-convert-any-object-to-string

                    System.out.println("\ntokenize: ");
                    tweet.setText(String.valueOf(Twokenize.tokenizeRawTweetText(tweet.getText())));
                    System.out.println(tweet.getText());
                    System.out.println("\nsentiment:" + tweet.getSentimentScore());

                    sentimentScore(tweet);
                    //twitterStatus.setSentimentType(analyzerService.analyse(text));

                    // monolingual slang dict
                    // https://github.com/ghpaetzold/questplusplus/blob/master/src/shef/mt/tools/mqm/resources/SlangDictionary.java

                    //System.out.println("\nanalyse():\n returns the distributed representation of the node \n" + analyse(tweet.getText()));
                    analyse(tweet.getText());
                    //BasicPipeline(tweet.getText());

                    /**
                    DocumentLex doc = null;

                    System.out.println("\ndoc.makeDocumentLex(tweet.getText():");
                    System.out.println("\nDoc text:\n" + tweet.getText());
                    System.out.println(doc.makeDocumentLex(tweet.getText()));
                    Map<String, Double> stringDoubleMap = NumericTweetFeatures.makeFeatures(tweet);

                    System.out.println("\nNumericTweetFeatures.makeFeatures(tweet):");
                    System.out.println(stringDoubleMap);
                     */

                    //System.out.println("\nTweet2VEC");
                    //new Tweet2vecModel(tweetsList);

                    List<String> doc1 = Arrays.asList("Lorem", "ipsum", "dolor", "ipsum", "sit", "ipsum");
                    List<String> doc2 = Arrays.asList("Vituperata", "incorrupte", "at", "ipsum", "pro", "quo");
                    List<String> doc3 = Arrays.asList("Has", "persius", "disputationi", "id", "simul");
                    List<List<String>> documents = Arrays.asList(doc1, doc2, doc3);

                    TFIDFCalculator calculator = new TFIDFCalculator();
                    double tfidf = calculator.tfIdf(doc1, documents, "earthquake");
                    System.out.println("TF-IDF (ipsum) = " + tfidf);





                }

            }



        } catch (IOException ie) {
            //SOPs
        }





    }




        /*

        InputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // https://stackoverflow.com/questions/54947356/how-can-i-iterate-through-json-objects-using-jackson
        ObjectMapper mapper = new ObjectMapper();
        // Cannot deserialize instance of `java.util.ArrayList<models.Tweet>` out of START_OBJECT token
        // cannot deserialize a single object into an array of objects
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true); // https://stackoverflow.com/questions/20837856/can-not-deserialize-instance-of-java-util-arraylist-out-of-start-object-token
        try {
            CollectionType tweetListType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Tweet.class);
            try (Stream<Tweet> tweets = NdJsonObjectMapper.readValue(is, Tweet.class)) {
                List<Tweet> tweetsList = mapper.readValue(file, tweetListType);
                tweetsList.forEach(System.out::println);


            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        */


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
            // 3  It returns the distributed representation of the node, which is a vector. This corresponds to the vectors a, b, c, p1, and p2 in Section 4 of the paper about the work: http://nlp.stanford.edu/pubs/SocherEtAl_EMNLP2013.pdf . It is not easily human interpretable, but a function of it predicts the node's sentiment, as explained in the paper.
            
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



    
  




}




        //public CompletionStage<Result> getLocation(String latitude, String longitude) {
    //    return ask(tweetActor, new tweetActor(latitude, longitude))
    //}


