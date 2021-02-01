package actors;

import Utils.inputOutput;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.chen0040.embeddings.GloVeModel;
import controllers.HomeController;
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
import play.libs.Json;
import features.NumericTweetFeatures;
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


import static Utils.inputOutput.convertFloatsToDoubles;
import static features.NumericTweetFeatures.makeFeatureVector;


public class jsonReader {
    //private static final List<Vector> featureVectorList = new ArrayList<>();




    /**
     * This method passes each file within the specified directory to parseEvent()
     */
    public void parse() {
        //String filename="lib/2020A_tweets/selected/";
        //Path pathToFile = Paths.get(filename);
        //System.out.println(pathToFile.toAbsolutePath());



    }


}