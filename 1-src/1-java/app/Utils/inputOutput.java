package Utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controllers.HomeController;
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
        //           "../../0-data/processed/" + file + ".txt"

        try(FileInputStream inputStream = new FileInputStream("../../0-data/processed/" + file + ".txt")) {
            return IOUtils.toString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void printVector(List<Tweet> tweetList) {
        PrintWriter out = null;

        // Export
        try {
            out = new PrintWriter(new FileWriter("../../0-data/processed/" + HomeController.StaticPath.output_file + ".txt", true), true);

        } catch (IOException e) {
            e.printStackTrace();
        }



        for (Tweet tweet : tweetList) {
            double[] d = convertFloatsToDoubles(tweet.getDimensions());
            if (tweet.getFeatureVector() != null) {

                // Print the feature vector
                assert out != null;
                out.print(tweet.getFeatureVector());


                 // Add the BERT Word Embeddings
                 out.print(",");
                 out.print("[");
                 for(double x : d){
                 out.print(x + ", ");
                 }
                 out.print("]");


            }
            assert out != null;
            out.println("");

        }
        assert out != null;
        out.flush();
        out.close();
        System.out.println("Exported to .txt");
    }


    public static double[] convertFloatsToDoubles(float[] input) {
        if (input == null)
        {
            return null; // Or throw an exception - your choice
        }
        double[] output = new double[input.length];
        for (int i = 0; i < input.length; i++)
        {
            output[i] = input[i];
        }
        return output;
    }

}
