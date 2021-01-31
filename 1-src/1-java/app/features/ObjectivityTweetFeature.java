package features;

import models.Tweet;

import java.util.LinkedHashSet;
import java.util.Set;

public class ObjectivityTweetFeature extends TweetFeature {

	public static Set<String> first_person_pronouns =
			new LinkedHashSet<>();
	static {
		first_person_pronouns.add("i");
		first_person_pronouns.add("me");
		first_person_pronouns.add("my");
		first_person_pronouns.add("mine");
		first_person_pronouns.add("myself");
		first_person_pronouns.add("we");
		first_person_pronouns.add("us");
		first_person_pronouns.add("our");
		first_person_pronouns.add("ours");
		first_person_pronouns.add("ourselves");
	}

	@Override
	// Classifies the tweet as either objective or not. (0 personal pronouns = objective).
	public boolean classify(Tweet tweet) {
		
		for ( String token : tweet.getTokens() ) {
			if ( first_person_pronouns.contains(token) ) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Returns the number of personal pronouns in the tweet.
	 * @param tweet
	 * @return
	 */
	public static double getScore(Tweet tweet) {
		double counter = 0;
		for ( String token : tweet.getTokens() ) {
			if ( first_person_pronouns.contains(token) ) {
				//System.out.println("hit");
				counter++;
			}
		}
		return counter;
	}


}
