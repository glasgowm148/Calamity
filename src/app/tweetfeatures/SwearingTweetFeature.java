package tweetfeatures;

import Utils.VoltLookUpTable;
import models.VoltTweet;

import java.io.File;

public class SwearingTweetFeature extends TweetFeature {
	
	public static VoltLookUpTable lookUpTable = 
			new VoltLookUpTable(new File("resources/badwords.txt" ));

	/**
	 * Checks if the tweet has a bad word or not
	 */
	public boolean classify(VoltTweet tweet) {
		for ( String token : tweet.getTokens() ) {
			if ( lookUpTable.contains(token) ) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the number of swearing tokens in the tweet
	 * @param tweet
	 * @return
	 */
	public static double getScore(VoltTweet tweet) {
		double counter = 0;
		for ( String token : tweet.getTokens() ) {
			if ( lookUpTable.contains(token) ) {
				counter++;
			}
		}
		return counter;
	}
	
}
