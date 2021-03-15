package actors;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


import com.fasterxml.jackson.databind.ObjectMapper;

import akka.actor.UntypedAbstractActor;
import com.twitter.twittertext.Extractor;
import com.twitter.twittertext.TwitterTextParseResults;
import com.twitter.twittertext.TwitterTextParser;

//import com.github.chen0040.embeddings.GloVeModel;
import models.GloVeModel;

//NLP
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import models.Tweet;
import models.LineProcessingResult;
import features.NumericTweetFeatures;
import messages.LineMessage;
//import utils.Twokenize;






public class LineProcessor extends  UntypedAbstractActor{

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof LineMessage) {

            // What data does each actor process?
            // System.out.println("Line: " + ((LineMessage) message).getData());
            // Thread number and the actor name relationship
            // System.out.println("Thread ["+Thread.currentThread().getId()+"] handling ["+ getSelf().toString()+"]");

            // get the message payload, this will be just one line from the  file
            List<String> messageData = ((LineMessage) message).getData();
            
        	List<Tweet> tweetList = new ArrayList<>();
            
            ObjectMapper mapper = new ObjectMapper();

            GloVeModel model = new GloVeModel();

            // Reads from the home directory
            model.load("lib/glove", 50);
            Properties props = new Properties();
            props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment"); // ner, entitymentions
            props.setProperty("parse.binaryTrees", "true");
            StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

            for(String tweetJson  : messageData) {
                Tweet tweet = mapper.readValue(tweetJson, Tweet.class);

                /* Extract Hashtags */
                final Extractor extractor = new Extractor();
                List<String> hashtags = extractor.extractHashtags(tweet.getText());
                tweet.setHashtags(hashtags);

                /* Text features using Twitter-Text */
                final TwitterTextParseResults result = TwitterTextParser.parseTweet(tweet.getText());
                tweet.setWeightedLength(result.weightedLength);
                tweet.setPermillage(result.permillage);

                // Tokenizer
                //List<String> tokens = Twokenize.tokenize(tweet.getText());
                //String[] str_array = tokens.toArray(new String[0]);
                //tweet.setTokens(str_array);

                /* Remove URLs, mentions, hashtags and whitespace */
                tweet.setText(tweet.getText().trim()
                        .replaceAll("http.*?[\\S]+", "")
                        .replaceAll("@[\\S]+", "")
                        .replaceAll("#", "")
                        .replaceAll("[\\s]+", " ")
                        //replace text between {},[],() including them
                        .replaceAll("\\{.*?}", "")
                        .replaceAll("\\[.*?]", "")
                        .replaceAll("\\(.*?\\)", "")
                        .replaceAll("[^A-Za-z0-9(),!?@'`\"_\n]", " ")
                        .replaceAll("[/]"," ")
                        .replaceAll(";"," "));

                Pattern charsPunctuationPattern = Pattern.compile("[\\d:,\"'`_|?!\n\r@;]+");
                String input_text = charsPunctuationPattern.matcher(tweet.getText().trim().toLowerCase()).replaceAll("");

                //Collect all tokens into labels collection.
                Collection<String> labels = Arrays.asList(input_text.split(" ")).parallelStream().filter(label->label.length()>0).collect(Collectors.toList());

                tweet.setTokens(labels);

                // GloVe Word Embeddings
                float[] d = model.encodeDocument(tweet.getText());
                tweet.setDimensions(d);

                // make the features
                Map<String, Double> stringDoubleMap = NumericTweetFeatures.makeFeatures(tweet);
                //System.out.println(stringDoubleMap);

                //System.out.println("\nSet Feature Vector\n");
                tweet.setFeatures(stringDoubleMap);
                //tweet.setFeatureVector(makeFeatureVector(stringDoubleMap));

                //System.out.println(stringDoubleMap);

                Annotation annotation = pipeline.process(tweet.getText());
                pipeline.annotate(annotation);
                for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                    Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                    tweet.setVectorTree(RNNCoreAnnotations.getNodeVector(tree)); //RNNCoreAnnotations.getPredictedClass(tree);
                }

                tweet.setSentiment(tweet.getVectorTree());

                tweetList.add(tweet);
                
            }
                //LineProcessingResult tweet = mapper.readValue(messageData, LineProcessingResult.class);


                // tell the sender that we got a result using a new type of message
                this.getSender().tell(new LineProcessingResult(tweetList), this.getSelf());
            
        } else {
            // ignore any other message type
            this.unhandled(message);
        }


    }

}


