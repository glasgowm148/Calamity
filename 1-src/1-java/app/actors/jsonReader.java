package actors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.chen0040.embeddings.GloVeModel;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;


import static tweetfeatures.NumericTweetFeatures.makeFeatureVector;


public class jsonReader {
    private static final List<Vector> featureVectorList = new ArrayList<>();

    private static final List<Tweet> tweetList  = new ArrayList<>();

    // This is the entry-point which passes each .jsonl file to parseEvent()
    public void parse() {
        try (Stream<Path> paths = Files.walk(Paths.get("../../0-data/raw/data/2020/2020-A/tweets/athens_earthquake"))) { //tweets/athens_earthquake  //testy
            paths.filter(Files::isRegularFile).forEach(jsonReader::parseEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void parseEvent(Path path)  {
        if(path.toString().contains("selected.jsonl") & !path.toString().matches(".*\\.gz")) {  //(".*\\.jsonl")
            System.out.println("Path:" + path);

            // Initialise actors for Word Embeddings & Sentiment Analysis
            GloVeModel model = new GloVeModel();
            model.load("../../1-src/1-java/lib/glove", 100);
            SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer();
            sentimentAnalyzer.initialize();

            try (InputStream is = new FileInputStream(String.valueOf(path))) {
                try (Stream<String> lines = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)).lines()) {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

                    // Iterate through each line in .jsonl and store it in a Tweet.class
                    for (String s : (Iterable<String>) lines::iterator) {
                        try {
                            JsonNode n = Json.parse(s);
                            Tweet tweet = mapper.treeToValue(n, Tweet.class); // here
                            tweetList.add(tweet);

                        } catch (JsonProcessingException jsonProcessingException) {
                            jsonProcessingException.printStackTrace();
                        }

                    }

                    System.out.println("Preparing to parse " + tweetList.size() + " tweets from "  + path);

                    // Keywords
                    featureActor featureActor = new featureActor();
                    featureActor.getKeywords(tweetList);

                    // Offset + Sentiment + TwitterText + Glove
                    int min = getMin();
                    tweetAnalyser(min, model, sentimentAnalyzer);

                    System.out.println("Parsed " + tweetList.size() + " tweets from "  + path);

                    printVector("brand_new_run2");
                }


            } catch (IOException e) {
                System.out.println(e.toString());
            }

        }
    }

    private static void printVector(String file) {
        PrintWriter out = null;

        // Export
        try {
            out = new PrintWriter(new FileWriter("../../0-data/processed/" + file + ".txt", true), true);

        } catch (IOException e) {
            e.printStackTrace();
        }


        for(Tweet tweet : tweetList) {
            double[] d = convertFloatsToDoubles(tweet.getDimensions());
            if(tweet.getFeatureVector() != null){
                assert out != null;

                // Print the feature vector
                out.print(tweet.getFeatureVector());

                // Add the BERT Word Embeddings
                out.print(",");
                out.print("[");
                for(double x : d){
                    out.print(x + ", ");
                }
                out.print("]");

            }
            assert out != null;
            out.println("");

        }
        assert out != null;
        out.flush();
        out.close();
        System.out.println("Exported to .txt");
    }

    private static void tweetAnalyser(int min, GloVeModel model, SentimentAnalyzer sentimentAnalyzer) {

        for(Tweet tweet : tweetList) {
            final Extractor extractor = new Extractor();
            List<String> hashtags = extractor.extractHashtags(tweet.getText());
            tweet.setHashtags(hashtags);

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

            } catch(NullPointerException e){
                e.printStackTrace();
                continue;
            }


            //semanticActor semanticActor = new semanticActor(tweet);
            //semanticActor.analyse(tweet.getText());

            // wordEmbeddings
            new gloveActor(model, tweet);

            // Time offset
            tweet.setOffset(((tweet.getCreatedAtInt() - min)));

            // make the features
            Map<String, Double> stringDoubleMap = NumericTweetFeatures.makeFeatures(tweet);

            //System.out.println(stringDoubleMap);

            tweet.setFeatures(stringDoubleMap);
            tweet.setFeatureVector(makeFeatureVector(stringDoubleMap));

            if(tweet.getFeatureVector() != null){
                featureVectorList.add(tweet.getFeatureVector());
            }




        }
    }

    private static int getMin() {
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