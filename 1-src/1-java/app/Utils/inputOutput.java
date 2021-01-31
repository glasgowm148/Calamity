package Utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Tweet;
import org.apache.commons.io.IOUtils;
import play.libs.Json;

import java.io.*;
import java.util.List;

public class inputOutput {

    public static String toPrettyFormat(File file) {
        String jsonText = null;
        try (
                FileInputStream is = new FileInputStream(file)
        ) {
            final JsonNode json = Json.parse(is);
            Tweet tweets = new Tweet(json);
            jsonText = tweets.toString();


            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            return gson.toJson(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonText;
    }

    public static String VectorToPrettyFormat(File file) {
        //             out = new PrintWriter(new FileWriter(, true), true);

        try(FileInputStream inputStream = new FileInputStream("../../0-data/processed/" + file + ".txt")) {
            return IOUtils.toString(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void printVector(String file, List<Tweet> tweetList) {
        PrintWriter out = null;

        // Export
        try {
            out = new PrintWriter(new FileWriter("../../0-data/processed/" + file + ".txt", true), true);

        } catch (IOException e) {
            e.printStackTrace();
        }



        for (Tweet tweet : tweetList) {
            //double[] d = convertFloatsToDoubles(tweet.getDimensions());
            if (tweet.getFeatureVector() != null) {
                assert out != null;

                // Print the feature vector
                out.print(tweet.getFeatureVector());

                /**
                 // Add the BERT Word Embeddings
                 out.print(",");
                 out.print("[");
                 for(double x : d){
                 out.print(x + ", ");
                 }
                 out.print("]");
                 */

            }
            assert out != null;
            out.println("");

        }
        assert out != null;
        out.flush();
        out.close();
        System.out.println("Exported to .txt");
    }

}
