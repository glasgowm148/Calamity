package controller;

import akka.actor.ActorSystem;
import controllers.HomeController;
import filters.infoRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import play.mvc.Result;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertNotNull;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.contentAsString;
/**
 * HomeCOntrollerUnitTest class tests the working of the HomeController.
 * @see HomeController
 * @see HomeCOntrollerUnitTest
 * @author Karan Behl
 */
public class HomeCOntrollerUnitTest {

    final ActorSystem actorSystem = ActorSystem.create( "system" ) ;

    //@InjectMocks annotation is used to create and inject the mock object
    @InjectMocks
    HomeController app = new HomeController(actorSystem);

    /**
     * Method to test the Result method in HomeController to check if mainPage.html is rendered properly or not
     * @author Karan Behl
     */
    @Test
    public void testResult(){
        MockitoAnnotations.initMocks( this );
        assertNotNull(true);
        Assert.assertEquals(app.index().status(), OK);
        Assert.assertEquals(app.index().contentType().get(),"text/html");
        Assert.assertEquals(app.index().charset().get(),"utf-8");
        Assert.assertEquals(contentAsString(app.index()).contains("Twitter Miner"),true);
    }



    /**
     * Method to test the GetGetUserProfile method in HomeController to check if tweets and profile info are obtained for the search query
     * @throws TwitterException
     * @throws ExecutionException
     * @throws InterruptedException
     * @author Anumeet
     */
    @Test
    public void  testGetUserProfile() throws TwitterException, ExecutionException, InterruptedException{
        CompletionStage<Result> result = app.getUserProfile("kaur_ish06");
        CompletableFuture<Result> r = result.toCompletableFuture();
        Assert.assertEquals(r.get().toString().length()>0,true );
    }

    /**
     * Method to test the GetGetLocationBasedTweets method in HomeController to check if tweets are obtained for the location search query
     * @throws TwitterException
     * @throws ExecutionException
     * @throws InterruptedException
     * @author Ishdeep
     */
    @Test
    public void  testGetLocationBasedTweets() throws TwitterException, ExecutionException, InterruptedException{
        CompletionStage<Result> result = app.getLocationBasedTweets("Montreal");
        CompletableFuture<Result> r = result.toCompletableFuture();
        Assert.assertEquals(r.get().toString().length()>0,true );
    }

    /**
     * Method to test the GetHashtagBasedTweets method in HomeController to check if tweets are obtained for the hashtag search query
     * @author Navdeep Kaur Brar
     * @throws TwitterException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void  testGetHashtagBasedTweets() throws TwitterException, ExecutionException, InterruptedException{
        CompletionStage<Result> result = app.getHashtagBasedTweets("%23MTL");
        CompletableFuture<Result> r = result.toCompletableFuture();
        Assert.assertEquals(r.get().toString().length()>0,true );
    }

    /**
     * Method to test the getDistinctWords method in HomeController to check if distinct words are obtained or not
     * @author Komaldeep Singh
     * @throws TwitterException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void  testgetDistinctWords() throws TwitterException, ExecutionException, InterruptedException{
        CompletionStage<Result> result = app.getDistinctWords("montreal");
        CompletableFuture<Result> r = result.toCompletableFuture();
        Assert.assertEquals(r.get().toString().length()>0,true );
    }

    //creating a mock object for infoRepository
    @Mock
    infoRepository model = new infoRepository();

    /**
     * @Before will execute before
     * each test method. The init method sets up mock values to return for the get methods.
     * @author Karan Behl
     */
    @Before
    public void init() {
        model.setId(12345678L);
        model.setName("Karan Behl");
        model.setScreenName("KaranbBehl");
        model.setSentiment(":-)");
        model.setLocation("Jalandhar, India");
        model.setDescription("just chilling");
        model.setFollowersCount(500);
        model.setFriendsCount(12);
        model.setHashtag("%23Yoman");
        model.setPublicUrl("https://twitter.com/Komalde36295870");
        model.setImageUrl("https://pbs.twimg.com/media/Djdbn3qU4AArmVi.jpg");
        List<String> tweetData = new ArrayList<String>();
        tweetData.add("Mockito Rocks students shock");
        model.setTweetData(tweetData);

    }

    /**
     * infoRepositorySetterUnitTest method tests the set methods in infoRepository class.
     */
    @Test
    public void infoRepositorySetterUnitTest() {
        Assert.assertEquals(model.getId(),12345678L );
        Assert.assertEquals(model.getName(), "Karan Behl");
        Assert.assertEquals(model.getScreenName(), "KaranbBehl");
        Assert.assertEquals(model.getSentiment(), ":-)");
        Assert.assertEquals(model.getLocation(), "Jalandhar, India");
        Assert.assertEquals(model.getDescription(),"just chilling" );
        Assert.assertEquals(model.getFollowersCount(),500);
        Assert.assertEquals(model.getFriendsCount(), 12);
        Assert.assertEquals(model.getHashtag(), "%23Yoman");
        Assert.assertEquals(model.getPublicUrl(), "https://twitter.com/Komalde36295870");
        Assert.assertEquals(model.getImageUrl(), "https://pbs.twimg.com/media/Djdbn3qU4AArmVi.jpg");
        List<String> tweetData = new ArrayList<String>();
        tweetData.add("Mockito Rocks students shock");
        Assert.assertEquals(model.getTweetData(), tweetData);
    }

}
