package tweetfeatures;

import Utils.VoltLookUpTable;
import models.Tweet;
import models.VoltTweet;

import java.io.File;

public class SlanginessTweetFeature extends TweetFeature {
	
	public static VoltLookUpTable lookUpTable = 
			new VoltLookUpTable(new File("resources/slangs.txt" ));

	@Override
	/**
	 * Checks if the tweet has a slangy word or not
	 */
	public boolean classify(Tweet tweet) {
		for ( String token : tweet.getTokens() ) {
			if ( lookUpTable.contains(token) ) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the number of slang tokens in the tweet
	 * @param tweet
	 * @return
	 */
	public static double getScore(Tweet tweet) {
		double counter = 0;
		for ( String token : tweet.getTokens() ) {
			if ( lookUpTable.contains(token) ) {
				counter++;
			}
		}
		return counter;
	}
	
}
