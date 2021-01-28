package controllers;

import java.io.*;

// Play
import play.mvc.Controller;
import play.mvc.Result;

// Project imports
import actors.*;
import logic.*;




public class HomeController extends Controller {

    public Result index()  {

        // Start timer
        long startTime = System.currentTimeMillis();

        // Parse into Tweet.class
        jsonReader reader = new jsonReader();
        reader.parse();

        // Uncomment this line to parse all tweets (resource intensive! - hours)
        //tweetList = reader.parseOne(pathAll);

        // Print elapsed time to console
        printTimer(startTime);

        // Prints to browser
        return ok(Sanitise.toPrettyFormat(new File("../../1-src/1-java/conf/10.jsonl")));
    }

    private void printTimer(long startTime) {
        // Outputs the elapsed time to console
        long elapsedTime = System.currentTimeMillis() - startTime;
        long elapsedSeconds = elapsedTime / 1000;
        long elapsedMinutes = elapsedSeconds / 60;
        System.out.println("Time elapsed: " + elapsedMinutes + " minutes");
        System.out.println(elapsedSeconds + " seconds");
    }




}

