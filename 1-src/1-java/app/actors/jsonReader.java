package actors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import logic.Sanitise;
import logic.SentimentAnalyzer;
import logic.Twokenize;
import models.SentimentResult;
import models.Tweet;
import play.libs.Json;
import tweetfeatures.NumericTweetFeatures;
import twitter.twittertext.Extractor;
import twitter.twittertext.TwitterTextParseResults;
import twitter.twittertext.TwitterTextParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;


import static tweetfeatures.NumericTweetFeatures.makeFeatureVector;


public class jsonReader {
    //private static final List<Vector> featureVectorList = new ArrayList<>();

    private static final List<Tweet> tweetList  = new ArrayList<>();

    public static Object parseEvent(String name) {
        System.out.println(name);
        return ":-|";
    }


    /**
     * @param path - the event path
     * Parses each event into Tweet model - Calculates the offset and event keywords before passing to tweetAnalyser()
     * @return
     */
    public static List<Tweet> parseEvent(Path path)  {

        // Ensure it's a selected.json file
        if(path.toString().contains("selected.jsonl") & !path.toString().matches(".*\\.gz")) {  //(".*\\.jsonl")


            // Initialise actors for Word Embeddings
            //GloVeModel model = new GloVeModel();
            //model.load("lib/glove", 100);

            // is = a FileInputStream of the path
            try (InputStream is = new FileInputStream(String.valueOf(path))) {

                // @lines = A Stream of strings
                try (Stream<String> lines = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)).lines()) {

                    // Instantiate a new ObjectMapper object
                    ObjectMapper mapper = new ObjectMapper();

                    // Configure the mapper to accept single values as arrays - so we can deserialise each line into an array
                    mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

                    // Iterate through each line
                    for (String s : (Iterable<String>) lines::iterator) {

                        try {

                            // @n = A JsonNode of the Tweet
                            JsonNode n = Json.parse(s);

                            // @tweet = JsonNode mapped to the Tweet model using JacksonXMLs 'treeToValue'
                            Tweet tweet = mapper.treeToValue(n, Tweet.class); // here

                            // @tweetList = An ArrayList we add all the to.
                            tweetList.add(tweet);

                        } catch (JsonProcessingException jsonProcessingException) {

                            // json Processing exceptions will be printed to console
                            jsonProcessingException.printStackTrace();
                        }

                    }

                    System.out.println("\n" + tweetList.size() + " tweets read into model from" + path);


                }


            } catch (IOException e) {
                System.out.println(e.toString());


            }
        }

        return tweetList;


    }


    /**
     * //@param model - word embeddings model
     * @param min - minimum time
     * @param sentimentAnalyzer - sentimentActor
 * For each tweet in the event, hashtags, text-features are extracted
 * The text is then sanitisied before sentiment analysis is performed.
     */
    public static void tweetAnalyser(int min, SentimentAnalyzer sentimentAnalyzer) throws InterruptedException { //GloVeModel model,

        //  tweetList = tweetList.stream().map(x -> "D").collect(Collectors.toList());

        // Iterate over each tweet in the collection
        for (Tweet tweet : new ArrayList<Tweet>(tweetList)) {

            // Extractor
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

            // Progress 'bar'
            System.out.print(".");


            try {
                SentimentResult sentimentResult = sentimentAnalyzer.getSentimentResult(tweet.getText());
                tweet.setPositiveSentiment(sentimentResult.getSentimentClass().getPositive());
                tweet.setNegativeSentiment(sentimentResult.getSentimentClass().getNegative());

            } catch (NullPointerException e) {
                e.printStackTrace();
                continue;
            }


            //semanticActor semanticActor = new semanticActor(tweet);
            //semanticActor.analyse(tweet.getText());

            // wordEmbeddings
            // new gloveActor(model, tweet);

            // Time offset
            tweet.setOffset(((tweet.getCreatedAtInt() - min)));

            // make the features
            Map<String, Double> stringDoubleMap = NumericTweetFeatures.makeFeatures(tweet);

            //System.out.println(stringDoubleMap);

            tweet.setFeatures(stringDoubleMap);
            tweet.setFeatureVector(makeFeatureVector(stringDoubleMap));

            //if(tweet.getFeatureVector() != null){
            //    featureVectorList.add(tweet.getFeatureVector());
            //}



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

    public static double[] convertFloatsToDoubles(float[] input) {
        if (input == null)
        {
            return null; // Or throw an exception - your choice
        }
        double[] output = new double[input.length];
        for (int i = 0; i < input.length; i++)
        {
            output[i] = input[i];
        }
        return output;
    }


}