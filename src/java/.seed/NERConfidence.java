package pipelines;

import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.pipeline.*;
import java.util.*;

public class NERConfidence {

    public static void main(String[] args) {
        String exampleText = "Joe Smith lives in California.";
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        CoreDocument document = new CoreDocument(exampleText);
        pipeline.annotate(document);
        // get confidences for entities
        for (CoreEntityMention em : document.entityMentions()) {
            System.out.println(em.text() + "\t" + em.entityTypeConfidences());
        }
        // get confidences for tokens
        for (CoreLabel token : document.tokens()) {
            System.out.println(token.word() + "\t" + token.get(CoreAnnotations.NamedEntityTagProbsAnnotation.class));
        }
    }
}

