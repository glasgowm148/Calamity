package tweetfeatures;

import Utils.VoltLookUpTable;
import models.Tweet;

import java.io.File;

public class IntensificationTweetFeature extends TweetFeature {

	public static VoltLookUpTable lookUpTable = 
			new VoltLookUpTable(new File("conf/intensifiers.txt" ));

	@Override
	/**
	 * Checks if the tweet has an intensification word
	 */
	public boolean classify(Tweet tweet) {
		for ( String token : tweet.getTokens() ) {
			if ( lookUpTable.contains((String) token) ) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the number of intensified tokens in the tweet
	 * @param tweet
	 * @return
	 */
	public static double getScore(Tweet tweet) {
		double counter = 0;
		for ( String token : tweet.getTokens() ) {
			if ( lookUpTable.contains((String) token) ) {
				counter++;
			}
		}
		return counter;
	}

}
