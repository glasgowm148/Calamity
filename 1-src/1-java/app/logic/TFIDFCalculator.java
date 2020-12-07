package logic;
import models.Tweet;

import java.util.List;

import static org.apache.commons.lang.StringUtils.split;


public class TFIDFCalculator {

    /**
     * @param doc  list of strings
     * @param term String represents a term
     * @return term frequency of term in document
     */
    public double tf(List<String> doc, String term) {
        double result = 0;
        for (String word : doc) {
            if (term.equalsIgnoreCase(word))
                result++;
        }
        return result / doc.size();
    }

    /**
     * @param docs list of list of strings represents the dataset
     * @param term String represents a term
     * @return the inverse term frequency of term in documents
     */
    public double idf(List<Tweet> docs, String term) {
        double n = 0;
        for (Tweet doc : docs) {
            for (String word : split(doc.getText())) {
                if (term.equalsIgnoreCase(word)) {
                    n++;
                    break;
                }
            }
        }
        return Math.log(docs.size() / n);
    }

    /**
     * @param doc  a text document
     * @param docs all documents
     * @param term term
     * @return the TF-IDF of term
     */
    public double tfIdf(List<String> doc, List<Tweet> docs, String term) {
        return tf(doc, term) * idf(docs, term);

    }

    public static void main(String[] args) {




    }


}