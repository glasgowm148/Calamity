package actors;

import akka.actor.UntypedAbstractActor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitter.twittertext.Extractor;
import com.twitter.twittertext.TwitterTextParseResults;
import com.twitter.twittertext.TwitterTextParser;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import features.NumericTweetFeatures;
import messages.LineMessage;
import models.GloVeModel;
import models.LineProcessingResult;
import models.Tweet;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class LineProcessor extends UntypedAbstractActor {

    private static final String resourceFilePath = "lib/conf/stopwords.txt";

    ObjectMapper mapper = new ObjectMapper();
    GloVeModel model = new GloVeModel();

    LineProcessor(){
        model.load("lib/glove", Integer.parseInt(System.getenv("NUMBER_OF_EMBEDDINGS")));
    }

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

            Properties props = new Properties();
            props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment"); // ner, entitymentions
            props.setProperty("parse.binaryTrees", "true");
            StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

            for (String tweetJson : messageData) {
                Tweet tweet = mapper.readValue(tweetJson, Tweet.class);

                /* Extract Hashtags */
                final Extractor extractor = new Extractor();
                List<String> hashtags = extractor.extractHashtags(tweet.getText());
                tweet.setHashtags(hashtags);
                System.out.println("Tweet ID: ");
                System.out.print(tweet.getId().toString());
                /* Text features using Twitter-Text */
                final TwitterTextParseResults result = TwitterTextParser.parseTweet(tweet.getText());
                tweet.setWeightedLength(result.weightedLength);
                tweet.setPermillage(result.permillage);
/*
                try {
                    // Remove URLs, mentions, hashtags and whitespace
                    tweet.setText(tweet.getText().trim()
                            .replaceAll("http.*?[\\S]+", "")
                            .replaceAll("@[\\S]+", "")
                            .replaceAll("#", "")
                            .replaceAll("[\\s]+", " ")
                            .replaceAll("\\{.*?}", "")
                            .replaceAll("\\[.*?]", "")
                            .replaceAll("\\(.*?\\)", "")
                            .replaceAll("[^A-Za-z0-9(),!?@'`\"_\n]", " ")
                            .replaceAll("[/]", " ")
                            .replaceAll(";", " "));

                    Pattern charsPunctuationPattern = Pattern.compile("[\\d:,\"'`_|?!\n\r@;]+");
                    String input_text = charsPunctuationPattern.matcher(tweet.getText().trim().toLowerCase()).replaceAll("");

                    //   Collect all tokens into labels collection.
                    Collection<String> labels = Arrays.asList(input_text.split(" ")).parallelStream().filter(label -> label.length() > 0).collect(Collectors.toList());

                    // Remove stopWords
                    labels.removeAll(getFileContentAsList());


                    tweet.setText(String.valueOf(labels));
                } catch (Exception e) {

                    System.out.println("Error: " + e.toString());
                    break;
                }
                */



                //ArrayList<String> defuzzedTokens = FeatureUtil.fuzztoken(String.valueOf(labels), true);
                /* call akka actor service */


                // GloVe Word Embeddings
                float[] d = model.encodeDocument(tweet.getText());
                tweet.setDimensions(d);

                // make the features - System.out.println(stringDoubleMap);
                Map<String, Double> stringDoubleMap = NumericTweetFeatures.makeFeatures(tweet);


                //System.out.println("\nSet Feature Vector\n");
                tweet.setFeatures(stringDoubleMap);

                // tweet.setFeatureVector(makeFeatureVector(stringDoubleMap));

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

    /**
     *
     * @return
     * @throws IOException
     */
    private List<String> getFileContentAsList() throws IOException {

        File file = ResourceUtils.getFile(LineProcessor.resourceFilePath);
        List<String> lines = Files.readAllLines(file.toPath());
        lines = lines.stream().map(String::toLowerCase).collect(Collectors.toList());

        return lines;

    }

}
/**
 * //StanfordLemmatizer lemmy = new StanfordLemmatizer();
 * //System.out.println(lemmy.lemmatize(tweet.getText()));
 * <p>
 * <p>
 * // Simple Sentence crashes the app for some reason?
 * //Sentence sent = new Sentence(tweet.getText());
 * //List<String> lemmas = sent.lemmas();
 * //System.out.println(lemmas);
 * <p>
 * <p>
 * <p>
 * // Tokenizer
 * //List<String> tokens = Twokenize.tokenize(tweet.getText());
 * //String[] str_array = tokens.toArray(new String[0]);
 * //System.out.println("tokens" + Arrays.toString(Arrays.stream(str_array).toArray()));
 * //tweet.setTokens(Arrays.asList(str_array));
 * // tweet.setText(Arrays.toString(str_array));
 * //StanfordSentiment senty = new StanfordSentiment();
 * //senty.vectorTree(tweet);
 */
