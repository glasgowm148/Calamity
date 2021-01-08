package classifiers;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.NamedEntityType;
import Utils.UnicodeConverter;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

public class NamedEntityClassifier {

    public static String serializedClassifier = "lib/english.all.3class.caseless.distsim.crf.ser.gz";
    public static AbstractSequenceClassifier<CoreLabel> classifier =
            CRFClassifier.getClassifierNoExceptions(serializedClassifier);

    public static Map<String, NamedEntityType> getNamedEntites(String text) {
        Map<String, NamedEntityType> namedEntites =
                new LinkedHashMap<>();

        text = UnicodeConverter.convert(text);
        for ( List<CoreLabel> lcl : classifier.classify(text) ) {

            for (CoreLabel cl : lcl) {
                String eTag = cl.get(CoreAnnotations.AnswerAnnotation.class);
                String namedEntity = cl.toString();
                namedEntity = namedEntity.toLowerCase();
                switch (eTag) {
                    case "LOCATION":
                        namedEntites.put(namedEntity, NamedEntityType.LOCATION);
                        break;
                    case "PERSON":
                        namedEntites.put(namedEntity, NamedEntityType.PERSON);
                        break;
                    case "ORGANIZATION":
                        namedEntites.put(namedEntity, NamedEntityType.ORGANIZATION);
                        break;
                }
            }
        }

        return namedEntites;
    }

    public Set<String> getLocations(String text) {
        Set<String> locations = new LinkedHashSet<>();
        text = UnicodeConverter.convert(text);
        for ( List<CoreLabel> lcl : classifier.classify(text) ) {

            for (CoreLabel cl : lcl) {
                String eTag = cl.get(CoreAnnotations.AnswerAnnotation.class);
                if ( eTag.equals("LOCATION") ) {
                    String location = cl.toString();
                    location = location.toLowerCase();
                    locations.add(location);
                }
            }
        }

        return locations;
    }

    public static void main(String[] args) {
        Map<String, NamedEntityType> ne =
                NamedEntityClassifier.getNamedEntites("Obama is in Syria in microsoft");

        for (String e : ne.keySet()) {
            NamedEntityType t = ne.get(e);
            System.out.println(e + " = " + t);
        }

    }

}