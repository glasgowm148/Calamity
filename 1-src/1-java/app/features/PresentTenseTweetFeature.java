package features;

import models.Tweet;

import java.util.LinkedHashSet;
import java.util.Set;

public class PresentTenseTweetFeature extends TweetFeature {
	
	public static Set<String> presence_words =
			new LinkedHashSet<>();

	static {
		presence_words.add( "am" );
		presence_words.add( "is" );
		presence_words.add( "are" );
		presence_words.add( "has" );
		presence_words.add( "have" );
		presence_words.add( "i'm" );
		presence_words.add( "he's" );
		presence_words.add( "she's" );
		presence_words.add( "it's" );
	}

	@Override
	/**
	 * Checks if the tweet has a present tense or not
	 */
	public boolean classify(Tweet tweet) {
		for ( String token : tweet.getTokens() ) {
			System.out.println("t" + token);
			if ( presence_words.contains(token) ) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the number of present tenses in the tweet
	 * @param tweet
	 * @return
	 */
	public static double getScore(Tweet tweet) {
		double counter = 0;
		for ( String token : tweet.getTokens() ) {
			if ( presence_words.contains(token) ) {
				counter++;
			}
		}
		return counter;
	}
	
}
