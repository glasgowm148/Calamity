package features;

import models.Tweet;

import java.util.LinkedHashSet;
import java.util.Set;

public class PastTenseTweetFeature extends TweetFeature {
	
	public static Set<String> past_words =
			new LinkedHashSet<>();
	
	static {
		past_words.add("was");
		past_words.add("were");
		past_words.add("had");
		//past_words.add("did");
	}

	@Override
	public boolean classify(Tweet tweet) {
		for ( String token : tweet.getTokens() ) {
			if ( past_words.contains(token) ) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the number of past tenses words in the tweet
	 * @param tweet
	 * @return
	 */
	public static double getScore(Tweet tweet) {
		double counter = 0;
		for ( String token : tweet.getTokens() ) {
			if ( past_words.contains(token) ) {
				counter++;
			}
		}
		return counter;
	}
	
}
