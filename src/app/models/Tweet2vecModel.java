package models;

import logic.StopWords;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.feature.Word2VecModel;
import org.apache.spark.mllib.linalg.Vector;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


public class Tweet2vecModel {
    public Tweet2vecModel(List<Tweet> list) {

        String logFile = "/Users/pseudo/Documents/GitHub/HelpMe/src/conf/uniqueTweetsNoHold.csv"; // Should be some file on your system

        SparkConf conf = new SparkConf().setAppName("FeatureVector").setMaster("local[2]");
        JavaSparkContext sc = new JavaSparkContext(conf); // java.lang.ExceptionInInitializerError: null


        JavaRDD<String> tweetText = StopWords.loadTwitterData(sc, logFile);
        //List<String> collectedList = tweetText.collect();
        List<String> collectedList = Arrays.asList("a test tweet", "also, a test tweet");

        Word2VecModel model = Word2VecModel.load(sc.sc(), "uniqueTweet.model");

        java.util.Map<String, List<Double>> finalVector = new HashMap<>();
        long count = 0;


        for (String tweet : collectedList) { //For each tweet
            double[] totalVectorValue;
            List<Double> theList = new ArrayList<>();
            String[] tokens = tweet.split(" ");

            for (String word: tokens) { //For each word in the tweet
                count++;
                Vector vectorValues = null;
                try{
                    vectorValues = model.transform(word);
                }catch (IllegalStateException e){
//                    e.printStackTrace();
                    count++;
                }
                if (vectorValues != null){
                    totalVectorValue = vectorValues.toArray();
                    for (double temp: totalVectorValue) {
                        theList.add(temp);
                    }
                }
                if (count == 30){
                    break;
                }
            }

            finalVector.put(tweet, theList);
        }

        int max = 300;

        for (java.util.Map.Entry<String, List<Double>> entry : finalVector.entrySet()) {
            List<Double> val = entry.getValue();

            for (int i = val.size(); i < max; i++) {
                val.add(0.0d);
            }
        }

        for (List<Double> temp: finalVector.values()) {
            if (max < (temp).size()){
                max = (temp).size();
            }

        }

        File file = new File("/Users/pseudo/Documents/GitHub/HelpMe/src/conf/agreedOnlyTweetsOutOf60KVectorsNoHold.csv"); //Output File
        FileWriter writer = null;
        // if file doesnt exists, then create it
        try{

            if (!file.exists()) {
                file.createNewFile();
            }

            writer = new FileWriter(file);


            for (java.util.Map.Entry<String, List<Double>> entry : finalVector.entrySet()) {
                List<Double> val = entry.getValue();

                String key = entry.getKey();
                writer.append(key);

                for (Double aDouble : val) {
                    writer.append(",");
                    writer.append(Double.toString(aDouble));
                }
                writer.append("\n");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            try {
                assert writer != null;
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}