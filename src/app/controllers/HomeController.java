package controllers;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Scheduler;
import akka.actor.typed.javadsl.AskPattern;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import logic.NDJson;
import logic.Sanitise;
import logic.SentimentAnalyzer;
import logic.Twokenize;
import models.SentimentResult;
import models.Tweet;
import org.json.JSONObject;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

import static com.google.common.collect.Iterables.size;

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

        /**
         * File is in NDJson format (One JSON object per line)
         * Reading it in with Iterable<String> currently
         */
        List<Tweet> tweetList = new ArrayList<>();
        List<JSONObject> tweetArray = new ArrayList<>();
        Object[] objArray;
        try (InputStream is = new FileInputStream(new File("conf/alberta.json"))) {

            try (Stream<String> lines = new BufferedReader(new InputStreamReader(is)).lines()) {
                tweetArray = NDJson.parse(is);
                ObjectMapper mapper1 = new ObjectMapper();

                System.out.println("\nText:\n" + tweet1.getText());
                List<Tweet> pp3 = mapper1.readValue((JsonParser) NDJson.parse(is), new TypeReference<List<Tweet>>() {});
                Tweet pp4 = mapper1.readValue((JsonParser) NDJson.parse(is), Tweet.class);
                System.out.println("\nAlternative..." + pp4.getText());

                // toArray() returns an array containing all of the elements in this list in the correct order
                objArray = tweetArray.toArray();

                System.out.println("Elements in Array :");
                for(int i=0; i < objArray.length ; i++) {
                    System.out.println(objArray[i]);
                }

                //pp4.stream().forEach(x -> System.out.println(x));

                ObjectMapper mapper = new ObjectMapper();
                for (String s : (Iterable<String>) lines::iterator) {

                    // Parses a string as JSON
                    int i = 0;
                    try {
                        JsonNode n = Json.parse(s);
                        Tweet tweet = mapper.treeToValue(n, Tweet.class);
                        System.out.println("\nText:\n" + tweet.getText());
                        tweetList.add(Sanitise(tweet));
                    } catch (JsonProcessingException jsonProcessingException) {

                        i += 1;
                        //jsonProcessingException.printStackTrace();
                    }
                    System.out.println("\nJsonProcessingExceptions:" + i);

                    // expected output: ReferenceError: nonExistentFunction is not defined
                    // Note - error messages will vary depending on browser



                    //System.out.println(n.asText());
                    //System.out.println("\nJsValue\n");
                    //JsValue j = JacksonJson.parseJsValue(s);
                    //System.out.println("\nJsValue\n" + j);


                    /**
                     * Binds JSON tree to Tweet Obj
                     * Tweet.java is a bit of a mess right now.
                     * I'd expected to find one premade but spent hours - couldn't find a comprehensive one anywhere?
                     *
                     */





                    /**
                     * Feature Vector
                     *
                     * Not 100% what this should look like,
                     *  1. one tuple per tweet in the set? [(x,y), (x,y), (x,y)]
                     *  2. one set per tweet?  [[(x,y), (x,y), (x,y)],[(x,y), (x,y), (x,y)]]


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

                    /**
                     * Next Steps
                     *
                     * 1. Priority is getting it into a feature vector.
                     *    It doesn't need to be a good feature vector.
                     *    Just some numbers I can export to Python and then start ML
                     *    Will retroactively improve feature vector based on success rate
                     *    Currently doing TF-IDF on dummytext.
                     *    a) Build a List<Tweet> with the loaded tweet
                     *    b) Pass to TFIDFCalculator()
                     *
                     *
                     * 2. I've got the string iterator into a Tweet model, but - for some reason it's only
                     *    pulling in the first 3 tweets? Something to do with the format? I'm concerned converting it
                     *    to a string then back into JSON - is going to cause issues going forward.
                     *
                     *  Note: I've copied two tweet sets into my /conf folder, there's labels and various other
                     *  related files.
                     */


                    // Disabled Sentiment Analysis & Feature Extraction for now.
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

                    /**
                     * Feature Extraction
                     */

                    //DocumentLex doc = null;

                    //System.out.println("\ndoc.makeDocumentLex(tweet.getText():");
                    //System.out.println("\nDoc text:\n" + tweet.getText());
                    //System.out.println(doc.makeDocumentLex(tweet.getText()));
                    //Map<String, Double> stringDoubleMap = NumericTweetFeatures.makeFeatures(tweet);

                    //System.out.println("\nNumericTweetFeatures.makeFeatures(tweet):");
                    //System.out.println(stringDoubleMap);


                }
                System.out.println("ForEach:");

            }
            System.out.println("Tweets imported:");
            System.out.println(size(tweetList));
            System.out.println("Tweets imported into Object Array:");
            System.out.println(objArray.length);




        } catch (IOException e) {
            //SOPs
            System.out.println(e.toString());
        }

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


}




        //public CompletionStage<Result> getLocation(String latitude, String longitude) {
    //    return ask(tweetActor, new tweetActor(latitude, longitude))
    //}


//System.out.println("\nJsonString\n");
//System.out.println(s);
//System.out.println(ToStringBuilder.reflectionToString(tweet)); // https://stackoverflow.com/questions/31847080/how-to-convert-any-object-to-string
//System.out.println(n);
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