package utils;

import actors.LineProcessor;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import models.Tweet;

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

    public void vectorTree(Tweet tweet) {
        LineProcessor.StanCoreNLP(pipeline, tweet);
    }
}

