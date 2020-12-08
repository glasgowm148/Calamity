package logic;
/*
https://github.com/WuLC/KeywordExtraction/blob/master/src/com/lc/nlp/keyword/algorithm/TFIDF.java
 */

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.common.Term;
import models.Tweet;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class TermFrequency
{
    private static final DecimalFormat df = new DecimalFormat("0.00");//format the output to reserve two decimal places

    private static final int keywordsNumber = 10;
    static final float d = 0.85f;           //damping factor, default 0.85
    static final int max_iter = 200;        //max iteration times
    static final float min_diff = 0.0001f;  //condition to judge whether recurse or not
    //number of keywords to extract,default 5
    private static final int coOccuranceWindow=3; //size of the co-occurance window, default 3
    private static final String[] providedKeywords = new String[]{"Russia", "fire", "Fire", "explosions", "choke", "burning", "wildfire", "wildfires", "Explosions"};
    //private static String[] providedKeywords = new String[]{"Avalanche", "landslide", "earthquake", "sinkhole", "fire", "wildfire", "volcanic eruption", "flood", "Tsunami"};

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
        HashMap<String, Integer> wordCount = new HashMap<>();
        HashMap<String, Float> TFValues = new HashMap<>();
        for(String word : words)
        {
            wordCount.merge(word, 1, Integer::sum);
        }

        int wordLen = words.size();

        //traverse the HashMap
        for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
            TFValues.put(entry.getKey(), Float.parseFloat(entry.getValue().toString()) / wordLen);
            System.out.println(entry.getKey().toString() + " = "+  Float.parseFloat(entry.getValue().toString()) / wordLen);
        }

        return TFValues;
    }


    public static boolean shouldInclude(Term term)
    {
        return CoreStopWordDictionary.shouldInclude(term);
    }

    public static HashMap<String,HashMap<String, Float>> tweetListTF(List<Tweet> tweetList)
    {

        HashMap<String, HashMap<String, Float>> allTF = new HashMap<String, HashMap<String, Float>>();
        for(Tweet tweet: tweetList){
            HashMap<String, Float> tweetTF = TermFrequency.getTF(tweet.getText());
            tweetTF = TermFrequency.getTF(tweet.getText());
            allTF.put(tweet.getIdStr(), tweetTF);
        }

        return allTF;
    }

    /**
     * get keywords of each tweet
     * @param tweetList(String): list of tweets
     * @return(Map<String,List<String>>): path of file and its corresponding keywords
     */
    public static Map<String,List<String>> getKeywords(List<Tweet> tweetList)
    {

        // calculate TF-IDF value for each word of each file under the dirPath
        Map<String, HashMap<String, Float>> liftTFIDF = new HashMap<String, HashMap<String, Float>>();

        // calls getDirTFIDF() & tweetListId()
        /** calls getDirTFIDF on the tweet list **/
        liftTFIDF = TermFrequency.getDirTFIDF(tweetList);
        System.out.println("liftTFIDF:" + liftTFIDF);

        Map<String,List<String>> tweetListKeywords = new HashMap<String,List<String>>();

        /**Concurrent Mod
        for (Tweet tweet:tweetList)
        {
            Map<String,Float> singlePassageTFIDF= new HashMap<String,Float>();
            singlePassageTFIDF = liftTFIDF.get(tweet.getId().toString());


            // Sort the keywords in terms of TF-IDF value in descending order
            List<Map.Entry<String,Float>> entryList=new ArrayList<Map.Entry<String,Float>>(singlePassageTFIDF.entrySet());


            entryList.sort(new Comparator<Map.Entry<String, Float>>() {
                @Override
                public int compare(Map.Entry<String, Float> c1, Map.Entry<String, Float> c2) {
                    return c2.getValue().compareTo(c1.getValue());
                }
            });

            // get keywords
            List<String> systemKeywordList=new ArrayList<String>();
            for(int k=0;k<keywordsNumber;k++)
            {
                try
                {
                    TimeUnit.SECONDS.sleep(1);

                    systemKeywordList.add(entryList.get(k).getKey());
                }
                catch(IndexOutOfBoundsException | InterruptedException e)
                {
                    e.printStackTrace();
                    continue;
                }

            }
            System.out.println("\ngetWordScore for:" + tweet.getIdStr() + "\n " + getWordScore(tweet) );
            List<Float> result = calculate(systemKeywordList, providedKeywords );
            System.out.println("\nPrecision, Recall, F-Measure for dict:\n" + result);

            tweet.setResult(result);
            tweetListKeywords.put(tweet.getId().toString(), systemKeywordList);
        }
         **/
        /** Returns a dictionary tweet_id:[keywords] **/
        System.out.println("\ntweetListKeywords:\n" + tweetListKeywords);
        return tweetListKeywords;
    }

    /**
     * calculate TF-IDF value for each word of each file under a directory
     * @return(Map<String, HashMap<String, Float>>): path of file and its corresponding "word:TF-IDF Value" pairs
     */
    public static Map<String, HashMap<String, Float>> getDirTFIDF(List<Tweet> tweetList)
    {
        HashMap<String, HashMap<String, Float>> dirFilesTF = new HashMap<String, HashMap<String, Float>>();
        HashMap<String, Float> dirFilesIDF = new HashMap<String, Float>();

        dirFilesTF = TermFrequency.tweetListTF(tweetList);
        dirFilesIDF = TermFrequency.tweetListIDF(tweetList);

        Map<String, HashMap<String, Float>> dirFilesTFIDF = new HashMap<String, HashMap<String, Float>>();
        Map<String,Float> singlePassageWord= new HashMap<String,Float>();
        List<String> fileList = new ArrayList<String>();
        for (Tweet tweet: tweetList)
        {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            HashMap<String,Float> temp= new HashMap<String,Float>();
            singlePassageWord = dirFilesTF.get(tweet.getId().toString());
            for (Map.Entry<String, Float> entry : singlePassageWord.entrySet()) {
                String word = entry.getKey();
                Float TFIDF = entry.getValue() * dirFilesIDF.get(word);
                temp.put(word, TFIDF);
            }
            dirFilesTFIDF.put(tweet.getId().toString(), temp);
        }
        /****/
        //System.out.println("\ndirFilesTFIDF:\n" + dirFilesTFIDF);
        return dirFilesTFIDF;
    }

    private static HashMap<String, Float> tweetListIDF(List<Tweet> tweetList) {
        List<String> fileList = new ArrayList<String>();
        int docNum = fileList.size();

        Map<String, Set<String>> passageWords = new HashMap<String, Set<String>>();

        // get words that are not repeated in the tweet
        for(Tweet tweet:tweetList)
        {
            List<Term> terms = new ArrayList<Term>();
            Set<String> words = new HashSet<String>();
            terms=HanLP.segment(tweet.getText());
            for(Term t:terms)
            {
                if(TermFrequency.shouldInclude(t))
                {
                    words.add(t.word);
                   // System.out.println(t.word + " added to corpus");
                }
            }
            passageWords.put(String.valueOf(tweet.getId()), words);
        }

        // get IDF values
        HashMap<String, Integer> wordPassageNum = new HashMap<String, Integer>();
        for(Tweet tweet:tweetList)
        {
            Set<String> wordSet = new HashSet<String>();
            wordSet = passageWords.get(tweet.getId().toString());
            for(String word:wordSet)
            {
                if(wordPassageNum.get(word) == null)
                    wordPassageNum.put(word,1);
                else
                    wordPassageNum.put(word, wordPassageNum.get(word) + 1);
            }
        }

        System.out.println("wordPassageNum:" + wordPassageNum);

        HashMap<String, Float> wordIDF = new HashMap<String, Float>();
        Iterator<Map.Entry<String, Integer>> iter_dict = wordPassageNum.entrySet().iterator();
        while(iter_dict.hasNext())
        {
            Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter_dict.next();
            float value = (float)Math.log( docNum / (Float.parseFloat(entry.getValue().toString())) );
            wordIDF.put(entry.getKey().toString(), value);
            /**This is returning infinity?? **/
            //System.out.println(entry.getKey().toString() + "=" +value);
        }
        return wordIDF;
    }
    /**
     * Takes the tweet and extracts a termList using HanLP
     * **/
    public static Map<String,Float> getWordScore(Tweet tweet)
    {

        //segment text into words
        List<Term> termList = HanLP.segment(tweet.getIdStr() + tweet.getText());

        int count=1;  //position of each word
        Map<String,Integer> wordPosition = new HashMap<String,Integer>();

        List<String> wordList=new ArrayList<String>();

        //filter stop words
        for (Term t : termList)
        {
            if (shouldInclude(t))
            {
                wordList.add(t.word);
                if(!wordPosition.containsKey(t.word))
                {
                    wordPosition.put(t.word,count);
                    count++;
                }
            }
        }
        System.out.println("Keyword candidates:"+wordList);

        //generate word-graph in terms of size of co-occur window
        Map<String, Set<String>> words = new HashMap<String, Set<String>>();
        Queue<String> que = new LinkedList<String>();
        for (String w : wordList)
        {
            if (!words.containsKey(w))
            {
                words.put(w, new HashSet<String>());
            }
            que.offer(w);    // insert into the end of the queue
            if (que.size() > coOccuranceWindow)
            {
                que.poll();  // pop from the queue
            }

            for (String w1 : que)
            {
                for (String w2 : que)
                {
                    if (w1.equals(w2))
                    {
                        continue;
                    }

                    words.get(w1).add(w2);
                    words.get(w2).add(w1);
                }
            }
        }
        System.out.println("word-graph:"+words); //each k,v represents all the words in v point to k

        // iterate till recurse
        Map<String, Float> score = new HashMap<String, Float>();
        for (int i = 0; i < max_iter; ++i)
        {
            Map<String, Float> m = new HashMap<String, Float>();
            float max_diff = 0;
            for (Map.Entry<String, Set<String>> entry : words.entrySet())
            {
                String key = entry.getKey();
                Set<String> value = entry.getValue();
                m.put(key, 1 - d);
                for (String other : value)
                {
                    int size = words.get(other).size();
                    if (key.equals(other) || size == 0) continue;
                    m.put(key, m.get(key) + d / size * (score.get(other) == null ? 0 : score.get(other)));
                }

                max_diff = Math.max(max_diff, Math.abs(m.get(key) - (score.get(key) == null ? 1 : score.get(key))));
            }
            score = m;

            //exit once recurse
            if (max_diff <= min_diff)
                break;
        }
        return score;
    }

    public static List<Float> calculate(List<String> tweetListKeywords,String[] manualKeywords)
    {

        int sysLen=tweetListKeywords.size();
        int manLen=manualKeywords.length;

        //Caculate.printKeywords(systemKeywords,manualKeywords);

        int hit=0;

        for (String tweetListKeyword : tweetListKeywords) {
            for (String manualKeyword : manualKeywords) {
                if (tweetListKeyword.equals(manualKeyword)) {
                    hit++;
                    break;
                }
            }
        }

        //Get Precision Value
        float pValue=(float)hit/sysLen;
        pValue*=100; //represent in the form of %

        //Get Recall Value
        float rValue=(float)hit/manLen;
        rValue*=100;

        //Get F-Measure
        float fValue;
        if(rValue==0 || pValue == 0)
            fValue=0;
        else
            fValue=2*rValue*pValue/(rValue+pValue);

        List<Float> result = new ArrayList<Float>();
        result.add(Float.parseFloat(df.format(pValue)));
        result.add(Float.parseFloat(df.format(rValue)));
        result.add(Float.parseFloat(df.format(fValue)));

        return result;
    }



}
