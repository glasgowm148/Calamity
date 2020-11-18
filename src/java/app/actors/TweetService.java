package actors;
import models.Tweet;
import play.*;
import play.mvc.*;
import play.db.jpa.*;
import java.util.List;
import java.util.Vector;

public class TweetService {

    private static Vector<Double> features;

    public void setFeatureVector(Vector<Double> features) {
        TweetService.features = features;
    }


    public static Vector<Double> getFeatureVector() {
        return features;
    }




}