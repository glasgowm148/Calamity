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
import logic.Sanitise;
import logic.SentimentAnalyzer;
import logic.Twokenize;
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
import java.io.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

import static pipelines.BasicPipeline.BasicPipeline;

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

        /**
        for (String tweet : tweetsList) {
            Sanitise.clean(tweet);
            System.out.println("\nCleanedTweet:\n" + tweet.getText());

            System.out.println(ToStringBuilder.reflectionToString(tweet)); // https://stackoverflow.com/questions/31847080/how-to-convert-any-object-to-string


            System.out.println("\nsentimentScore:\n" + tweet.getSentimentScore());

            // twitterStatus.setSentimentType(analyzerService.analyse(text));

            // monolingual slang dict
            // https://github.com/ghpaetzold/questplusplus/blob/master/src/shef/mt/tools/mqm/resources/SlangDictionary.java

            System.out.println("\nanalyse():\n" + analyse(tweet.getText()));

            System.out.println("\ntokenize():\n" + Twokenize.tokenizeRawTweetText(tweet.getText()));

            try {
                BasicPipeline(tweet.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }

            //BagOfWords(ArrayList<String> dir, ArrayList<String> lex, String sPath
            //BagOfWords nlp = new NLPAlgorithms.BagOfWords(directories, lex, stopWords);

            System.out.println("\ndoc.makeDocumentLex(tweet.getText():");
            DocumentLex doc = null;
            try {
                doc = new DocumentLex();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert doc != null;
            System.out.println(doc.makeDocumentLex(tweet.getText()));
            Map<String, Double> stringDoubleMap = NumericTweetFeatures.makeFeatures(tweet);

            System.out.println("\nNumericTweetFeatures.makeFeatures(tweet):");
            System.out.println(stringDoubleMap);

            //System.out.println("\nTweet2VEC");
            //new Tweet2vecModel(tweetsList);



        }*/

        // Outputs to browser
        return ok(prettyJson);
    }

    public List<Tweet> ParseJSON(File file) {

        System.out.println("Parsing JSON...");
        List<Tweet> tList =  new ArrayList<>();


        try (InputStream is = new FileInputStream(new File("/Users/pseudo/Documents/GitHub/HelpMe/src/conf/before_selection.json"))) {
            try (Stream<String> lines = new BufferedReader(new InputStreamReader(is)).lines()) {

                ObjectMapper mapper = new ObjectMapper();
                for (String s : (Iterable<String>) lines::iterator) {

                    //System.out.println("\nJsonString\n");
                    //System.out.println(s);

                    //System.out.println("\nJsValue\n");
                    //JsValue j = JacksonJson.parseJsValue(s);
                    //System.out.println(j);

                    //System.out.println("\nJsonNode\n");
                    JsonNode n = Json.parse(s);
                    //System.out.println(n);

                    Tweet tweet = mapper.treeToValue(n, Tweet.class);
                    System.out.println("\ntweet\n");
                    System.out.println(tweet.getText());

                    Sanitise.clean(tweet);
                    System.out.println("\nCleanedTweet:\n" + tweet.getText());

                    System.out.println(ToStringBuilder.reflectionToString(tweet)); // https://stackoverflow.com/questions/31847080/how-to-convert-any-object-to-string


                    System.out.println("\nsentimentScore:\n" + tweet.getSentimentScore());

                    // twitterStatus.setSentimentType(analyzerService.analyse(text));

                    // monolingual slang dict
                    // https://github.com/ghpaetzold/questplusplus/blob/master/src/shef/mt/tools/mqm/resources/SlangDictionary.java

                    System.out.println("\nanalyse():\n" + analyse(tweet.getText()));

                    System.out.println("\ntokenize():\n" + Twokenize.tokenizeRawTweetText(tweet.getText()));

                    BasicPipeline(tweet.getText());

                    //DocumentLex doc = null;

                    //System.out.println("\ndoc.makeDocumentLex(tweet.getText():");
                    //System.out.println(doc.makeDocumentLex(tweet.getText()));
                    //Map<String, Double> stringDoubleMap = NumericTweetFeatures.makeFeatures(tweet);

                    //System.out.println("\nNumericTweetFeatures.makeFeatures(tweet):");
                    //System.out.println(stringDoubleMap);

                    //System.out.println("\nTweet2VEC");
                    //new Tweet2vecModel(tweetsList);




                    //tList = mapper.readValue(newJsonNode, new TypeReference<List<Tweet>>() {});

                }

            }

            //tList = Arrays.asList(mapper.readValue(s, Tweet[].class));
            //List<Tweet> ppl2 = Arrays.asList(mapper.readValue(s, Tweet[].class));
            //System.out.println("\nJSON array to List of objects");
            //ppl2.stream().forEach(x -> System.out.println(x.getText()));
            //Tweet t = mapper.readValue(s, Tweet.class);
            //tList.add(t);

            //Stream<Tweet> readValue = ndJsonObjectMapper.readValue(is, Tweet.class);
            //readValue.forEach(s -> System.out::println(s.getText());
            //List<String> result = readValue.collect(Collectors.toList());
            //readValue.forEach(tweet-> System.out.println(tweet.getText()));

        } catch (IOException ie) {
            //SOPs
        }


        System.out.println("\nAlternative...");
        tList.forEach(System.out::println);
        //System.out.println(tList.length());
        System.out.println(tList.size());
        return tList;


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
            System.out.println("word:" + word + "lemma: " + lemma + "pos: " + pos + "ne: " + ne + "normalised " + normalized);
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


