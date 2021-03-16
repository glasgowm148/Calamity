package features;

import models.Tweet;
import utils.LookUpTable;

import java.io.File;

public class IntensificationTweetFeature extends TweetFeature {

    public static LookUpTable lookUpTable =
            new LookUpTable(new File("lib/conf/intensifiers.txt"));

    /**
     * Returns the number of intensified tokens in the tweet
     *
     * @param tweet
     * @return
     */
    public static double getScore(Tweet tweet) {
        double counter = 0;
        for (String token : tweet.getTokens()) {
            if (lookUpTable.contains(token)) {
                counter++;
            }
        }
        return counter;
    }

    @Override
    /**
     * Checks if the tweet has an intensification word
     */
    public boolean classify(Tweet tweet) {
        for (String token : tweet.getTokens()) {
            if (lookUpTable.contains(token)) {
                return true;
            }
        }
        return false;
    }

}
