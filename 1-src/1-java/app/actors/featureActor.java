package actors;

import logic.TermFrequency;
import models.Tweet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class featureActor {
    
    private static final int keywordsNumber = 5;
    static final float d = 0.85f;           //damping factor, default 0.85
    static final int max_iter = 200;        //max iteration times
    static final float min_diff = 0.0001f;  //condition to judge whether recurse or not
    //number of keywords to extract,default 5
    private static final int coOccuranceWindow=3; //size of the co-occurance window, default 3
    private static final String[] providedKeywords = new String[]{"Russia", "fire", "Fire", "explosions", "choke", "burning", "wildfire", "wildfires", "Explosions"};
    private static final DecimalFormat df = new DecimalFormat("0.00");//format the output to reserve two decimal places


    public featureActor(){

    }


    public Map<String, List<String>> getKeywords(List<Tweet> tweetList)
    {

        // calculate TF-IDF value for each word of each file under the dirPath
        Map<String, HashMap<String, Float>> liftTFIDF = new HashMap<String, HashMap<String, Float>>();

        /** Returns the inverse document frequency for the full tweet list **/
        liftTFIDF = TermFrequency.getDirTFIDF(tweetList);
        //System.out.println("liftTFIDF:" + liftTFIDF);

        // For each tweet
        Map<String,List<String>> tweetListKeywords = new HashMap<>();
        for (Tweet tweet:tweetList)
        {
            Map<String,Float> singlePassageTFIDF;
            singlePassageTFIDF = liftTFIDF.get(tweet.getId().toString());
            //System.out.println("singlePassageTFIDF" + singlePassageTFIDF );


            // Sort the keywords in terms of TF-IDF value in descending order
            List<Map.Entry<String,Float>> entryList= new ArrayList<>(singlePassageTFIDF.entrySet());
            entryList.sort((c1, c2) -> c2.getValue().compareTo(c1.getValue()));

            // Add a prespecified number of keywords to the systemKeywordList
            List<String> systemKeywordList= new ArrayList<>();
            for(int k=0;k<keywordsNumber;k++)
            {
                try
                {
                    systemKeywordList.add(entryList.get(k).getKey());
                }
                catch(IndexOutOfBoundsException e)
                {
                    //e.printStackTrace();
                }
            }
            // Calculate the Precision, Recall and F-Measure against the providedKeywords
            List<Float> result = calculate(systemKeywordList, providedKeywords );

            // Sets the result in the Tweet.class
            tweet.setResult(result);

            // Builds a dictionary tweet_id=[term, term]
            tweetListKeywords.put(tweet.getId().toString(), systemKeywordList);

            //System.out.println("\ngetWordScore for:" + tweet.getIdStr() + "\n " + getWordScore(tweet) );
            //System.out.println("\nPrecision, Recall, F-Measure for dict:\n" + result);
        }
        //System.out.println("\ntweetListKeywords:\n" + tweetListKeywords);
        return tweetListKeywords;
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
