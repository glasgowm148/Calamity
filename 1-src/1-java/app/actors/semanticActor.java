package actors;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import logic.SentimentAnalyzer;
import models.SentimentResult;
import models.Tweet;

import java.io.Serializable;
import java.util.Properties;

public class semanticActor {



    public semanticActor(Tweet tweet){
        SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer();
        sentimentAnalyzer.initialize();
        SentimentResult sentimentResult = sentimentAnalyzer.getSentimentResult(tweet.getText());
        tweet.setPositiveSentiment(sentimentResult.getSentimentClass().getPositive());
        tweet.setNegativeSentiment(sentimentResult.getSentimentClass().getNegative());
    }



    private void Sentiment(Tweet tweet) {
        /**
         * Sentiment Analysis
         */
        // SentimentScore
        // https://github.com/Ruthwik/Sentiment-Analysis


        // print results to console
        /*
        System.out.println("Sentiment Score: " + sentimentResult.getSentimentScore());
        System.out.println("Sentiment Type: " + sentimentResult.getSentimentType());
        System.out.println("Very positive: " + sentimentResult.getSentimentClass().getVeryPositive());
        System.out.println("Positive: " + sentimentResult.getSentimentClass().getPositive());
        System.out.println("Neutral: " + sentimentResult.getSentimentClass().getNeutral());
        System.out.println("Negative: " + sentimentResult.getSentimentClass().getNegative());
        System.out.println("Very negative: " + sentimentResult.getSentimentClass().getVeryNegative());
        tweet.setSentiment(sentimentResult);
        */


        //System.out.println("\n###sentiment:" + tweet.getSentimentScore());
        //twitterStatus.setSentimentType(analyzerService.analyse(text));

        // Slang
        // https://github.com/ghpaetzold/questplusplus/blob/master/src/shef/mt/tools/mqm/resources/SlangDictionary.java

        // Analyse()
        System.out.println("\nanalyse():\n returns the distributed representation of the node \n" + analyse(tweet.getText()));
        // returns the distributed representation of the node, which is a vector.
        // This corresponds to the vectors a, b, c, p1, and p2 in Section 4 of the paper about the work: http://nlp.stanford.edu/pubs/SocherEtAl_EMNLP2013.pdf . It is not easily human interpretable, but a function of it predicts the node's sentiment, as explained in the paper.
        tweet.setAnalysis(analyse(tweet.getText()));

        // Dependency Graph +
        // BasicPipeline(tweet.getText());
    }


    public Serializable analyse(String tweet) {
        // https://aboullaite.me/stanford-corenlp-java/

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment"); // ner, entitymentions
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Annotation annotation = pipeline.process(tweet);
        //props.setProperty("ssplit.eolonly", "true");
        props.setProperty("parse.binaryTrees", "true");
        pipeline.annotate(annotation);
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            // word
            // System.out.println(sentence.get(CoreAnnotations.TextAnnotation.class));
            //System.out.println(sentence.get(SentimentCoreAnnotations.SentimentClass.class));

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
}