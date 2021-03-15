package controllers;


import play.mvc.Controller;
import play.mvc.Result;
import servicesImp.ServicesImp;

import java.util.ArrayList;
import java.util.List;


/**
 * This controller contains an action to handle HTTP requests to the
 * application's home page.
 */


public class HomeController extends Controller {

    // Start timer for tracking efficiency
    static long startTime = System.currentTimeMillis();
    private final ServicesImp service = new ServicesImp();

    /* Entry point for /tweets */
    public Result index() throws Exception {

        /* call akka actor service */
        String result = service.akkaActorApi();

        service.saveResultInFile(result);

        return ok(result);
    }

    /**
     * @returns The saved file to /stored_tweets
     * to avoid expensive calls on /tweets
     */
    public Result explore() {
        // read the file that contains the result of / tweet
        return ok(service.contentSavedFile(StaticPath.saveFile));
    }

    public Result tutorial() {
        return ok("");
    }

    public static class StaticPath {

        public static List<String> tweets = new ArrayList<>();
        public static String path = "data/tweets/";
        public static String saveFile = "data/savedFile.json";

    }

}
