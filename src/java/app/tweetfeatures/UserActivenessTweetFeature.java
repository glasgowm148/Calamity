package tweetfeatures;

import models.Tweet;
import org.joda.time.DateTime;
import org.joda.time.Days;

public class UserActivenessTweetFeature extends TweetFeature {

	@Override
	public boolean classify(Tweet tweet) {
		int tweetsCount = tweet.getUserNumbTweets();
		double daysSinceReg = getScore(tweet);
		double ratio = (double) tweetsCount / daysSinceReg;
		return ratio > 1;
	}
	
	/**
	 * Returns the days of registration since the tweet was sent.
	 * @param tweet
	 * @return
	 */
	public static double getScore(Tweet tweet) {
		double daysSinceReg = 0;
		DateTime currentDate = new DateTime(tweet.getCreatedAt().getTime());
		DateTime regDate = new DateTime(tweet.getUserRegistrationDate().getTime());
		daysSinceReg = Days.daysBetween(regDate, currentDate).getDays();
		return daysSinceReg;
	}
	
	public static String getName() {
		return TweetFeature.active_user;
	}

}
