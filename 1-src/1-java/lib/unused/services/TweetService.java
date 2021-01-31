package services;
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