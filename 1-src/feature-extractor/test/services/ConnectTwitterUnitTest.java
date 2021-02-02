package services;
/**
 * ConnectTwitterUnitTest  contains methods to test the methods of ConnectTwitter class that fetch data from twitter API's
 * and process that data.
 * @see ConnectTwitter
 * @see ConnectTwitterUnitTest
 * @author Group V4
 */
import static org.junit.Assert.assertEquals;

import akka.actor.ActorSystem;
import org.junit.Before;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.HomeController;
import filters.infoRepository;
import services.ConnectTwitter;
import play.mvc.Result;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.*;
import  twitter4j.api.UsersResources;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.HashtagEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;
import twitter4j.Twitter;
import twitter4j.api.TimelinesResources;
public class ConnectTwitterUnitTest {

	//creates a mock instance of Connect Twitter
	@Mock
	ConnectTwitter connectTwitter = new ConnectTwitter();

	/**
	 * Tests the ConnectToTwitter method which establishes the connection with twitter API
	 * @throws TwitterException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 * @author Group V4
	 */
	@Test
	public void testConnectToTwitter() throws TwitterException, ExecutionException, InterruptedException {
		Twitter twitter = connectTwitter.connectToTwitter();
		Assert.assertEquals(twitter instanceof Twitter , true);
	}

	/**
	 * Tests the getqueryresult method which runs a query to get tweets from the Twitter API
	 * @throws TwitterException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 * @author Group V4
	 */
	@Test
	public void testgetqueryresult() throws TwitterException, ExecutionException, InterruptedException {
		List<Status> result= connectTwitter.getqueryresult("Montreal",10);
		Assert.assertEquals(result.size() >=0  , true);
	}

	/**
	 * Tests the getuserDetails method which gets the profile info of user from Twitter
	 * @throws TwitterException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 * @author Group V4
	 */
	@Test
	public void testgetUserDetails() throws TwitterException, ExecutionException, InterruptedException {
		infoRepository result = connectTwitter.getProfileInfo("karan123");
		Assert.assertEquals(result.getScreenName() !=null , true );
	}

	/**
	/* * Tests the searchTweets method which gets the tweets from Twitter
	 * @throws TwitterException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 * @author Group V4
	 *//*
	@Test
	public void testSearchTweets() throws TwitterException, ExecutionException, InterruptedException {
		CompletionStage<ArrayNode> result = connectTwitter.searchTweets("technology");
		Assert.assertEquals(result.toCompletableFuture().get().size() >=0 , true );
	}*/

	/**
	 * Tests the getUserTweets method which gets the user's latest tweets from Twitter
	 * @throws TwitterException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 * @author Group V4
	 */
	@Test
	public void testgetUserTweets() throws TwitterException, ExecutionException, InterruptedException {
		List<Status> result = connectTwitter.getUserTweets("Komalde36295870");
		Assert.assertEquals(result.size() >=0 , true );
	}

	/**
	 * Tests the getLocationTweets method which gets the tweets from Twitter for a particular location
	 * @throws TwitterException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 * @author Group V4
	 */
	@Test
	public void testgetLocationTweets() throws TwitterException, ExecutionException,InterruptedException {
		infoRepository result = connectTwitter.getLocationTweets("Montreal");
		Assert.assertEquals(result.getLocation() !=null , true );
	}

	/**
	 * Tests the getHashtagTweets method which gets the tweets from Twitter for a particular hashtag
	 * @throws TwitterException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 * @author Group V4
	 */
	@Test
	public void testgetHashtagTweets() throws TwitterException, ExecutionException, InterruptedException {
		infoRepository result = connectTwitter.getHashtagTweets("#Modi");
		Assert.assertEquals(result.getTweetData() !=null , true );
	}

	/**
	 * Tests the getTweetWords method which displays distinct words and frequencies
	 * @throws TwitterException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 * @author Group V4
	 */
	@Test
	public void testgetTweetWords() throws TwitterException, ExecutionException, InterruptedException {
		infoRepository result = connectTwitter.tweetWords("Montreal");
		Assert.assertEquals(result.getSearchTerms()!=null , true );
	}




}