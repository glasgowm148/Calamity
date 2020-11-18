package Utils;




import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

//import edu.stanford.nlp.pipeline.CoreDocument;

/**
 *
 * @author Punkid PC
 */
public class StanfordAnalysis extends SyntacticAnalysis{

    private StanfordCoreNLP pipeline;
   // private CoreDocument document;
    private Annotation annDocument;


    public StanfordAnalysis(String text){
        super(text);
    }

    private void makeDocument(HashMap<String, String> properties){
        Properties prop = new Properties();
        properties.keySet().forEach((k) ->{
            prop.setProperty(k, properties.get(k));
        });
        pipeline = new StanfordCoreNLP(prop);
        annDocument = new Annotation(text);
        pipeline.annotate(annDocument);
    }

    @Override
    public List<String> getSentences(String text) {
        HashMap<String,String> prop = new HashMap<>();
        prop.put("annotators", "tokenize,ssplit");
        prop.put("ssplit.isOneSentence", "true");
        prop.put("parse.model", "edu/stanford/nlp/models/srparser/englishSR.ser.gz");
        prop.put("tokenize.language", "en");
        makeDocument(prop);
        ArrayList<String> temp = new ArrayList<>();
        List<CoreMap> sentences = annDocument.get(SentencesAnnotation.class);
        for(CoreMap sentence: sentences){
            temp.add(sentence.toString());
        }
        return temp;
    }

    @Override
    public ArrayList<Sentence> getAnnotatedPOS(String text) {
        HashMap<String,String> prop = new HashMap<>();
        prop.put("annotators", "tokenize,ssplit,pos");
        prop.put("ssplit.isOneSentence", "true");
        prop.put("parse.model", "edu/stanford/nlp/models/srparser/englishSR.ser.gz");
        prop.put("tokenize.language", "en");
        makeDocument(prop);
        ArrayList<Sentence> tagger = new ArrayList<>();
        List<CoreMap> sentences= annDocument.get(SentencesAnnotation.class);
        ArrayList<SentenceToken> words = new ArrayList<>();
        for(CoreMap sentence: sentences){
            words.clear();
            for(CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)){
                String key = token.get(CoreAnnotations.TextAnnotation.class);
                String value = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                words.add(new SentenceToken(key,value));
            }
            tagger.add(new Sentence(words));
        }
        return tagger;

    }

    @Override
    public ArrayList<Sentence> getLemmatizedPOS(String text) {
        HashMap<String,String> prop = new HashMap<>();
        prop.put("annotators", "tokenize,ssplit,pos,lemma");
        prop.put("ssplit.isOneSentence", "true");
        prop.put("parse.model", "edu/stanford/nlp/models/srparser/englishSR.ser.gz");
        prop.put("tokenize.language", "en");
        makeDocument(prop);
        ArrayList<Sentence> tagger = new ArrayList<>();
        List<CoreMap> sentences= annDocument.get(SentencesAnnotation.class);
        ArrayList<SentenceToken> words = new ArrayList<>();
        for(CoreMap sentence: sentences){
            words.clear();
            for(CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)){
                String key = token.get(CoreAnnotations.TextAnnotation.class);
                String value = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                words.add(new SentenceToken(key,value));
            }
            tagger.add(new Sentence(words));
        }
        return tagger;
    }

}