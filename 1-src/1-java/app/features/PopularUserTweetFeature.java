package features;

import models.Tweet;

public class PopularUserTweetFeature extends TweetFeature {

	@Override
	/**
	 * Classifies the user as either popular or not
	 */
	public boolean classify(Tweet tweet) {
		double ratio = getScore(tweet);
		boolean b = ratio > 1;
		return b;
	}
	
	/**
	 * Returns the popularity ratio follower/friends
	 * @param tweet
	 * @return
	 */
	public static double getScore(Tweet tweet) {
		double popularityRatio = 0;
		int followers = tweet.getUserFollowersCount();
		int friends = tweet.getUserFriendsCount();
		popularityRatio = (double) followers/friends;//means his followers are more than his friends.
		return popularityRatio;
	}
	
}
