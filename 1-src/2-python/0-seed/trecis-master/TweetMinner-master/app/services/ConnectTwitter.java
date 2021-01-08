package services;

import play.libs.Json;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import filters.infoRepository;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.HashtagEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ConnectTwitter class contains all the methods required to access the twitter API and get required data.
 * @version 1.0
 * @author Navdeep Kaur Brar
 */
public class ConnectTwitter {

	/**
	 * Retrieve twitter object with some default configuration.
	 * @return A Twitter object
	 * @author Komaldeep Singh
	 */
	public static Twitter connectToTwitter() {
		ConfigurationBuilder twittercb = new ConfigurationBuilder();
		twittercb.setDebugEnabled(true)
		.setOAuthConsumerKey("1AuzFRQVdSOIDWa7Jl9xl8tCS")
        .setOAuthConsumerSecret("Opb5LycsGkx7u2LvJKXZDKosx6eQhCgjwNi0oYtTP2wGXfSWs7")
        .setOAuthAccessToken("1023051987150168064-Qkr749T4FQw5SeAnwLzYXxeRfwWfQY")
        .setOAuthAccessTokenSecret("H33ep7uAdJ4pdppkA6IfP42NOnlLXmVFcq6weWbKz2r8g");

		Twitter twitterObj = new TwitterFactory(twittercb.build()).getInstance();
		return twitterObj;
	}

	/**
	 * Retrieve the instance of Status Type with list of tweet status's.
	 * @param keyword The search terms of type string for which the tweets have to be searched.
	 * @param count The number of tweet status required of type int
	 * @return Status Type List
	 * @throws TwitterException
	 * @throws InterruptedExcetion
	 * @throws ExecutionException
	 * @author Navdeep Kaur Brar
	 */
	public static List<Status> getqueryresult(String keyword, int count) throws TwitterException, ExecutionException,InterruptedException {
		Twitter twitterObj = connectToTwitter();
		Query newQuery = new Query(keyword);
		List<Status> tweets = new ArrayList<Status>();
		newQuery.setCount(count);
		try {
			 QueryResult result = twitterObj.search(newQuery);
			 tweets = result.getTweets();
				
		}
		catch(TwitterException e) {
			e.printStackTrace();
			}
		return tweets;
	}

	/**
	 * Retrieve the twitter user's ten latest tweets
	 * @param username The username of the twitter user which is of type string.
	 * @return Status Type List
	 * @throws TwitterException 
	 * @throws InterruptedExcetion
	 * @throws ExecutionException
	 * @author Anumeet
	 */
	public static List<Status> getUserTweets(String username) throws TwitterException, ExecutionException,InterruptedException {
	
		Twitter twitterObj = connectToTwitter();
		List<Status> tweetsTen = new ArrayList<Status>();
		try {
			User twitterUser = twitterObj.showUser(username);
			Paging page = new Paging();
			page.setCount(10);
			tweetsTen = twitterObj.getUserTimeline(twitterUser.getId(),page);
		}
		catch(TwitterException e)
		{
			e.printStackTrace();
			}
		
		return tweetsTen;
	}

	/**
	 * Retrieve the instance of infoRepository with user profile info and ten latest tweets of user.
	 * @param username The username of the twitter user which is of type string.
	 * @return infoRepository
	 * @throws TwitterException 
	 * @throws InterruptedExcetion
	 * @throws ExecutionException
	 * @author Anumeet
	 */
	public static infoRepository getProfileInfo(String username) throws TwitterException, ExecutionException,InterruptedException {

		Twitter twitterObj = connectToTwitter();
		User twitterUser = twitterObj.showUser(username);
		List<Status> tweetData = getUserTweets(username);
		List<String> list = new ArrayList<String> ();
		tweetData.forEach((data) -> {
			list.add(data.getText());
		});
		infoRepository info = new infoRepository();
		info.setId(twitterUser.getId());
		info.setName(twitterUser.getName());
		info.setScreenName(twitterUser.getScreenName());
		info.setLocation(twitterUser.getLocation());
		info.setDescription(twitterUser.getDescription());
		info.setFollowersCount(twitterUser.getFollowersCount());
		info.setFriendsCount(twitterUser.getFriendsCount());
		info.setPublicUrl(twitterUser.getURL());
		info.setImageUrl(twitterUser.getBiggerProfileImageURL());
		info.setTweetData(list);
		return info;
	}

	/**
	 * Retrieve the instance of infoRepository with ten latest tweets for the clicked location.
	 * @param location The location of type string for which tweets have to searched.
	 * @return infoRepository
	 * @throws TwitterException 
	 * @throws InterruptedExcetion
	 * @throws ExecutionException
	 * @author Ishdeep
	 */
	public static infoRepository getLocationTweets(String location) throws TwitterException, ExecutionException,InterruptedException {

		infoRepository info = new infoRepository();
		info.setLocation(location);
		List<Status> tweetsTen = getqueryresult(location,10);
		List<String> list = new ArrayList<String> ();
		tweetsTen.forEach((data) -> {
			list.add(data.getText());
		});
		info.setTweetData(list);
		return info;
	}

	/**
	 * Retrieve the instance of infoRepository with ten latest tweets for the clicked hashtag.
	 * @param hashtag The hashtag of type string for which tweets have to searched.
	 * @return infoRepository
	 * @throws TwitterException 
	 * @throws InterruptedExcetion
	 * @throws ExecutionException
	 * @author Navdeep Kaur Brar
	 */
	public static infoRepository getHashtagTweets(String hashtag) throws TwitterException, ExecutionException, InterruptedException {

		infoRepository info = new infoRepository();
		info.setHashtag("#"+hashtag.substring(1));
		List<Status> tweetsTen = getqueryresult(hashtag,10);
		List<String> list = new ArrayList<String> ();
		tweetsTen.forEach((data) -> {
			list.add(data.getText());
		});
		info.setTweetData(list);
		return info;
	}

	/**
	 * Retrieve the instance of infoRepository with distinct words and their frequencies
	 * @param keyword The search terms of type string for which tweets have to searched.
	 * @return infoRepository
	 * @throws TwitterException 
	 * @throws InterruptedExcetion
	 * @throws ExecutionException
	 * @author Komaldeep Singh
	 */
	public static infoRepository tweetWords(String keyword) throws TwitterException, ExecutionException,InterruptedException
	{

		infoRepository info = new infoRepository();
		info.setSearchTerms(keyword);
		List<Status> tweetsHundred = getqueryresult(keyword,100);
		ArrayList<String> finalList = getWordList(tweetsHundred);
		info.setCountList(finalList);
		return info;
	}

	/**
	 * Retrieve the ArrayList of type string with distinct words and their frequencies
	 * @param tweetsHundred The list of hundred tweet status's
	 * @return A String type Arraylist.
	 * @author Komaldeep Singh
	 */
	public static ArrayList<String> getWordList(List<Status> tweetsHundred)
	{
		ArrayList<String> tweetsList = new ArrayList<>();
		tweetsHundred.forEach((tweet) -> {
			tweetsList.add(tweet.getText()); 	
		});
		Map<String, Integer> wordList = 
				tweetsList.stream()
				.map(r -> r.split(" "))
				.flatMap(Arrays::stream)
				.map(r -> r.toLowerCase()
						.replaceAll("[\\W+]", ""))
				.collect(Collectors.toMap(x -> x, x -> 1, Integer::sum));
		Comparator<Map.Entry<String, Integer>> byCount = 
				Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder());
		Comparator<Map.Entry<String, Integer>> byAlphabet = 
				Comparator.comparing(Map.Entry::getKey);

		ArrayList<String> finalList= new ArrayList<>();
		wordList.entrySet().stream()
		.sorted(byCount.thenComparing(byAlphabet))
		.forEachOrdered(x -> {
			if(!(x.getKey().equals(""))) {
				finalList.add(x.getKey() + ": " +x.getValue().toString());	}
		});
		return finalList;	
	}

	/**
     * getSentiment method to calculate the sentiment from the tweets got from twitter based on the keyword searched.
     * @param keyword The search term for which the tweets and sentiments are displayed.
     * @return A String Type.
     * @throws TwitterException 
	 * @throws InterruptedExcetion
	 * @throws ExecutionException
     * @author Karan Behl
     */
	public static String getSentiment(String keyword)throws TwitterException, ExecutionException, InterruptedException {
		String emotion ="";	
		List<Status> tweets = getqueryresult(keyword, 100);
		ArrayList<String> twt = new ArrayList<>();
		int happy=0, sad=0;
		tweets.forEach((tweet) -> {
			twt.add(tweet.getText()); 	
		});
		happy = (int) twt.stream()
				.filter(line -> line.contains(":)") || line.contains(":-)"))
				.count();
		sad = (int) twt.stream()
				.filter(line -> line.contains(":(") || line.contains(":-("))
				.count();
		float size = twt.size();
		float happypec = ((happy/size)*100);
		float sadpec = ((sad/size)*100);
		if(happypec>=70)
			emotion = ":-)";
		else if(sadpec>=70)
			emotion = ":-(";
		else emotion = ":-|";
			return emotion;
	}
}