package actors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Tweet;
import play.libs.Json;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public class jsonReader {
    String UTF8 = "UTF8";
    int BUFFER_SIZE = 8192;

    //private final File path = new File("../../0-data/raw/data/2020/2020-A/tweets/whaley_bridge_collapse/selected.jsonl");
    private final File path = new File("/Users/mark/Documents/GitHub/HelpMe/1-src/1-java/conf/10.jsonl");
    List<Tweet> tweetList = new ArrayList<>();

    public List<Tweet> readJson() {
        /** Get all .json files within a directory **/
        //List<Path> bList = collectFiles();
        //tweetList = parseAll(bList);

        /** Parse into Tweet.class **/
        tweetList = parseOne();
        System.out.println("\ntweetList.size():\n" + tweetList.size());
        return tweetList;
    }


    public List<Tweet> parseOne()  {
        try (InputStream is = new FileInputStream(path)) {
            try (Stream<String> lines = new BufferedReader(new InputStreamReader(is, UTF8)).lines()) {
                return parseTweet(lines);
            }


        } catch (IOException e) {
            System.out.println(e.toString());
        }



        return null;
    }
    public List<Tweet> parseAll(List<Path> bList)  {
        for (Path l : bList){
            try (InputStream is = new FileInputStream(String.valueOf(l))) {
                try (Stream<String> lines = new BufferedReader(new InputStreamReader(is)).lines()) {

                    return parseTweet(lines);

                }


            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }

        return null;
    }


    public List<Tweet> parseTweet(Stream<String> lines) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);


        for (String s : (Iterable<String>) lines::iterator) {
            // Parses a string as JSON
            try {
                JsonNode n = Json.parse(s);
                Tweet tweet = mapper.treeToValue(n, Tweet.class); // here
                tweetList.add(tweet); // Sanitise.clean(tweet);
            } catch (JsonProcessingException jsonProcessingException) {
                jsonProcessingException.printStackTrace();
            }

        }

        return tweetList;
    }
}
