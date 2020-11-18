package logic;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class FeatureVector {
    List<String> topics = new ArrayList<String>();
    List<String> places = new ArrayList<String>();
    List<String> keyWords = new ArrayList<String>();


    public FeatureVector(List<String> topics, List<String> places, String body) {
        this.topics = topics;
        this.places = places;
        this.keyWords = getKeyWords(body);


    }

    public void printRefinedData() {
        File file = new File("FeatureVector#1.csv");
        FileWriter fw;
        try {
            fw = new FileWriter(file, true);
            PrintWriter pw = new PrintWriter(fw);
            pw.print(topics + ", " + places + ", " + keyWords);
            pw.println();
            pw.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }


    public static List<String> getKeyWords(String body) {
        Set<String> stopWords = getStopWords();
        List<String> keyWords = new ArrayList<String>();
        body = body.replaceAll("[0-9]", "");
        body = body.toLowerCase();
        String[] words = body.split("[[ ]*|[,]*|[\\.]*|[:]*|[>]*|[<]*|[>]*|[/]*|[!]*|[?]*|[+]*|[;]*|[&]*|[(]*|[)]*|[\"]]+");


        for (int i = 0; i < words.length - 1; i++) {
            if (!stopWords.contains(words[i]) && words[i] != "") {

                keyWords.add(words[i]);
            }
        }

        return keyWords;
    }

    static Set<String> getStopWords() {
        File file = new File("conf/stopWords.txt");
        Set<String> stopWords = new TreeSet<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                stopWords.add(line);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return stopWords;

    }
}