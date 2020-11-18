package logic;


import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.common.Term;

import java.util.*;




public class TermFrequency
{

    /**
     * calculate TF value of each word in the tweet text
     * @param text(String): content of file
     * @return(HashMap<String, Float>): "words:TF value" pairs
     */
    public static HashMap<String, Float> getTF(String text)
    {
        List<Term> terms;
        ArrayList<String> words = new ArrayList<>();

        terms=HanLP.segment(text);

        // Remove stopwords
        for(Term t:terms)
        {
            if(TermFrequency.shouldInclude(t))
            {
                words.add(t.word);
            }
        }

        // get TF values
        HashMap<String, Integer> wordCount = new HashMap<String, Integer>();
        HashMap<String, Float> TFValues = new HashMap<String, Float>();
        for(String word : words)
        {
            if(wordCount.get(word) == null)
            {
                wordCount.put(word, 1);
            }
            else
            {
                wordCount.put(word, wordCount.get(word) + 1);
            }
        }

        int wordLen = words.size();

        //traverse the HashMap
        for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
            TFValues.put(entry.getKey(), Float.parseFloat(entry.getValue().toString()) / wordLen);
            //System.out.println(entry.getKey().toString() + " = "+  Float.parseFloat(entry.getValue().toString()) / wordLen);
        }

        return TFValues;
    }


    /**
     * judge whether a word belongs to stop words
     * @param term(Term): word needed to be judged
     * @return(boolean):  if the word is a stop word,return false;otherwise return true
     */
    public static boolean shouldInclude(Term term)
    {
        return CoreStopWordDictionary.shouldInclude(term);
    }





}
