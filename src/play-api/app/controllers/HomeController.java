package controllers;

import models.GloVeModel;
import play.mvc.Controller;
import play.mvc.Result;
import services.ServicesImp;

import java.util.ArrayList;
import java.util.List;


/**
 * This controller handles HTTP requests to the
 * application's home page.
 */
public class HomeController extends Controller {

    private final ServicesImp service = new ServicesImp();

    // Start timer for tracking efficiency
    static long startTime = System.currentTimeMillis();

    /* Entry point for /tweets */
    public Result index() throws Exception {

        // Instantiate a new Akka API service
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
        // Reads from the home directory

        GloVeModel model = new GloVeModel();
        model.load("lib/glove", Integer.parseInt(System.getenv("NUMBER_OF_EMBEDDINGS")));

        float[] d = model.encodeDocument("test sentence to encode");
        System.out.println("test sentence");

        return ok(service.contentSavedFile(StaticPath.saveFile));
    }

    public Result tutorial() {
        return ok("");
    }

    public static class StaticPath {

        public static List<String> tweets = new ArrayList<>();
        public static String path = System.getenv("TWEET_DIR");
        public static String saveFile = "data/savedFile.json";

    }

}
