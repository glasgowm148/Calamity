package utils;

import actors.LineProcessor;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import models.Tweet;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class StanfordSentiment {

    protected StanfordCoreNLP pipeline;

    public StanfordSentiment() {
        // Create StanfordCoreNLP object properties, with POS tagging
        // (required for lemmatization)
        Properties props;
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");

        // StanfordCoreNLP loads a lot of models, so you probably
        // only want to do this once per execution
        this.pipeline = new StanfordCoreNLP(props);
    }

    public void vectorTree(Tweet tweet)
    {
        LineProcessor.StanCoreNLP(pipeline, tweet);
    }
}

