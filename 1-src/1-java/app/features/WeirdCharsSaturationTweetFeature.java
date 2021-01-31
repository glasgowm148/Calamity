package features;

import models.Tweet;

public class WeirdCharsSaturationTweetFeature extends TweetFeature {

	/**
	 * Returns true if the number of weird characters is higher than
	 * half of the tweet's text size.
	 */
	public boolean classify(Tweet tweet) {
		double numbOfWeirdChars = getScore(tweet);

		return numbOfWeirdChars > (tweet.getText().length() / 2);
	}
	
	/**
	 * Returns the number of non-letter characters in the tweet
	 * @param tweet
	 * @return
	 */
	public static double getScore( Tweet tweet ) {
		double numbOfWeirdChars = 0;
		for ( Character c : tweet.getText().toCharArray() ) {
			if ( !Character.isLetter(c) ) {
				numbOfWeirdChars++;
			}
		}
		return numbOfWeirdChars;
	}
	
}
