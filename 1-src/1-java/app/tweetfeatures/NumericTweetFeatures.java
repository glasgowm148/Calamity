package tweetfeatures;

import classifiers.NamedEntityClassifier;
import models.Tweet;

import java.util.*;

/**
 * Extracts features from the Tweet.class into a numeric feature vector
 */

public class NumericTweetFeatures {
	public static Set<String> numericFeaturesNames;
	// tweet
	public static final String tweet_created_at = "tweet_created_at";
	public static final String tweet_id_str = "tweet_id_str";

	public static final String sent_from_mobile = "sent_from_mobile";
	// tweet-text
	public static final String tweet_length = "tweet_length";
	public static final String numb_of_urls = "numb_of_urls";
	public static final String numb_of_media = "numb_of_media";
	public static final String numb_of_hashtags = "numb_of_hashtags";
	public static final String numb_of_personal_pronouns = "numb_of_personal_pronouns";
	public static final String numb_of_present_tenses = "numb_of_present_tenses";
	public static final String numb_of_past_tenses = "numb_of_past_tenses";
	public static final String numb_of_named_entites = "numb_of_named_entites";
	public static final String numb_of_weird_chars = "numb_of_weird_chars";
	public static final String numb_of_questions = "numb_of_questions";
	public static final String numb_of_emoticons = "numb_of_emoticons";
	public static final String numb_of_swearing_words = "numb_of_swearing_word";
	public static final String numb_of_slang_words = "numb_of_slang_words";
	public static final String numb_of_intensifiers = "numb_of_intensifiers";
	public static final String numb_of_mentions = "numb_of_mentions";
	// user
	public static final String userFollowersCount = "userFollowersCount";
	public static final String userFriendsCount = "userFriendsCount";
	public static final String userRegistrationDays = "userRegistrationDays";
	public static final String user_numb_of_tweets = "user_numb_of_tweets";  /// empty
	public static final String numb_of_user_description_chars = "numb_of_user_description_chars";
	public static final String user_listed_count = "user_list_count";
	private static final String is_verified = "is_verified";
	// tweet-text-analysis
	public static final String positive_sentiment = "positive_sentiment";
	public static final String negative_sentiment = "negative_sentiment";
	private static final String tfidf_fire = "tfidf_fire";
	private static final String dict_precision = "dict_precision";
	private static final String dict_recall = "dict_recall";
	private static final String dict_f_measure = "dict_f_measure";
	private static final String offset = "offset";
	private static final String weighted_length = "weighted_length";
	private static final String permillage = "permillage";
	// UD
	//private static final String dimensionsBERT = "dimensionsBERT";
	//public static final String sent_from_web = "sent_from_web";   /// empty
	//public static final String has_geolocation = "has_geolocation";



	static {
		numericFeaturesNames = new LinkedHashSet<>();
		numericFeaturesNames.add(tweet_created_at);
		numericFeaturesNames.add(tweet_id_str);
		numericFeaturesNames.add(positive_sentiment);
		numericFeaturesNames.add(negative_sentiment);
		numericFeaturesNames.add(numb_of_mentions);
		numericFeaturesNames.add(numb_of_urls);
		numericFeaturesNames.add(numb_of_media);
		numericFeaturesNames.add(numb_of_hashtags);
		numericFeaturesNames.add(numb_of_personal_pronouns);
		numericFeaturesNames.add(numb_of_present_tenses);
		numericFeaturesNames.add(numb_of_past_tenses);
		numericFeaturesNames.add(numb_of_named_entites);
		numericFeaturesNames.add(sent_from_mobile);
		//numericFeaturesNames.add(sent_from_web);
		numericFeaturesNames.add(numb_of_weird_chars);
		numericFeaturesNames.add(numb_of_questions);
		numericFeaturesNames.add(numb_of_emoticons);
		numericFeaturesNames.add(numb_of_swearing_words);
		numericFeaturesNames.add(numb_of_slang_words);
		numericFeaturesNames.add(numb_of_intensifiers);
		//numericFeaturesNames.add(has_geolocation);
		numericFeaturesNames.add(tweet_length);
		numericFeaturesNames.add(userFollowersCount);
		numericFeaturesNames.add(userFriendsCount);
		numericFeaturesNames.add(userRegistrationDays);
		numericFeaturesNames.add(user_numb_of_tweets);
		numericFeaturesNames.add(numb_of_user_description_chars);
		numericFeaturesNames.add(user_listed_count);
		numericFeaturesNames.add(tfidf_fire);
		numericFeaturesNames.add(dict_precision);
		numericFeaturesNames.add(dict_recall);
		numericFeaturesNames.add(dict_f_measure);
		numericFeaturesNames.add(offset);
		numericFeaturesNames.add(weighted_length);
		numericFeaturesNames.add(permillage);
		numericFeaturesNames.add(is_verified);
		//numericFeaturesNames.add(dimensionsBERT);

		// is_retweet
		// caps_ratio
		//  "has_place",

	}




	public static Map<String, Double> makeFeatures(Tweet tweet) {
		Map<String, Double> features = new LinkedHashMap<>();

		features.put(tweet_created_at, (double)tweet.getCreatedAt().getTime());
		features.put(tweet_id_str, (double) Long.parseLong(tweet.getIdStr()));
		features.put(positive_sentiment, tweet.getPositiveSentiment());
		features.put(negative_sentiment, tweet.getNegativeSentiment());
		
		features.put(numb_of_mentions, (double) tweet.getUserMentions().size());
		features.put(numb_of_media, (double) tweet.getMedia().size());
		features.put(numb_of_urls, (double) tweet.getUrls().size());
		features.put(numb_of_hashtags, (double) tweet.getHashtags().size());
		features.put(numb_of_personal_pronouns, ObjectivityTweetFeature.getScore(tweet));
		features.put(numb_of_present_tenses, PresentTenseTweetFeature.getScore(tweet));
		features.put(numb_of_past_tenses, PastTenseTweetFeature.getScore(tweet));

		features.put(numb_of_named_entites, (double)
		NamedEntityClassifier.getNamedEntites(tweet.getText().replace("#", " ")).size());

		//features.put(sent_from_mobile, tweet.isSentFromMobile()?1.0:0);
		//features.put(sent_from_web, tweet.isSentFromWeb()?1.0:0);
		features.put(numb_of_weird_chars, WeirdCharsSaturationTweetFeature.getScore(tweet));
		
		int numbOfQuestions = 0;
		for ( char c : tweet.getText().toCharArray() ) {
			if ( c == '?' )
				numbOfQuestions++;
		}
		features.put(numb_of_questions, (double) numbOfQuestions);
		features.put(numb_of_emoticons,  EmoticonsTweetFeature.getScore(tweet));
		features.put(numb_of_swearing_words, SwearingTweetFeature.getScore(tweet));
		features.put(numb_of_slang_words, SlanginessTweetFeature.getScore(tweet));
		features.put(numb_of_intensifiers, IntensificationTweetFeature.getScore(tweet));
		//features.put(has_geolocation, tweet.getGeoLocation().length>0?1.0:0);
		features.put(tweet_length, (double)tweet.getText().length());
		features.put(userFollowersCount, (double) tweet.getUserFollowersCount());
		features.put(userFriendsCount, (double) tweet.getUserFriendsCount());
		//features.put(userRegistrationDays, UserActivenessTweetFeature.getScore(tweet));
		features.put(user_numb_of_tweets, (double)tweet.getUserNumbTweets());
		//features.put(numb_of_user_description_chars, (double)tweet.getUserDescription().length());
		features.put(user_listed_count, (double)tweet.getUserListedCount());
		//features.put(tfidf_fire, tweet.getTFIDF());
		features.put(dict_precision, (double) tweet.getResult(0));
		features.put(dict_recall, (double) tweet.getResult(1));
		features.put(dict_f_measure, (double) tweet.getResult(2));
		features.put(offset, tweet.getOffset());
		features.put(weighted_length, (double)tweet.getWeightedLength());
		features.put(permillage, (double)tweet.getPermillage());
		features.put(is_verified, (double) tweet.getIsVerified());
		//features.put(dimensionsBERT, tweet.getDimensions()); - currently appended manually




		return features;
	}

	public static Vector<Double> makeFeatureVector(Map<String, Double> features){
		//System.out.println(features);
		return new Vector<>(features.values());

	}
}









