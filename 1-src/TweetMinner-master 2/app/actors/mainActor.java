package actors;

import play.libs.Json;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import twitter4j.*;
import twitter4j.Status;
import actors.sentimentActor;
import filters.infoRepository;
import akka.actor.*;
import static akka.pattern.Patterns.ask;
import javax.inject.*;
import static services.ConnectTwitter.connectToTwitter;
import akka.actor.ActorSystem;
import akka.dispatch.*;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.Await;
import scala.concurrent.Promise;

public class mainActor extends AbstractActor {

	/**
	 * mainActor class connects with Twitter and fetches 10 latest tweets for the search query.
	 * Scheduler is used to search the query at a fixed interval to check for any new tweets and display those
	 * which makes the application reactive.
	 * @version 1.0
	 * @see mainActor
	 * @author Karan Behl
	 */
	public final ActorRef ac;
	private final Twitter twitterInstance = connectToTwitter();
	private ActorRef sentiment_actor;
	private static String emotion;

	//list variable to store the tweet results and check for duplicacy
	List <Status> tweetlist= new ArrayList<Status>();
	//int variable to keep check on the query being searched
	int flag=0;

	//Actor reference created
	public static Props props(ActorRef ac) {
		return Props.create(mainActor.class, ac);
	}

	public mainActor(ActorRef ac) {
		this.ac=ac;
	}

	/**
	 * createRecieve method for mainActor that runs a query on Twitter to fetch 10 latest tweets for search terms and then continue
	 * searching for any updates.
	 * @author Navdeep Kaur Brar
	 * @author Karan Behl
	 * @author Komaldeep Singh
	 */
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(String.class, searchKeys -> {
					Runnable r = new Runnable() {
						@Override
						public void run() {

							try {
								ActorSystem system = ActorSystem.create();
								ActorRef sentiment_actor = system.actorOf(sentimentActor.getProps());
								Future<Object> future =ask(sentiment_actor, new sentimentActor.Sentiment(searchKeys), 3000);
								future.onComplete(new OnComplete<Object>(){
									public void onComplete(Throwable t, Object result){
										emotion = result.toString();
									}
								}, system.dispatcher());
							}catch(Exception e)
							{}
							if(flag==0)
							{
								flag=1;
								Query twitterQuery1 = new Query(searchKeys);
								twitterQuery1.setCount(10);
								QueryResult tweetsResult1 = null;
								try {
									tweetsResult1 = twitterInstance.search(twitterQuery1);

								} catch (TwitterException e) {
									e.printStackTrace();
								}

								List<Status> tweetsTen = tweetsResult1.getTweets();
								int count1=tweetsTen.size();
								if (count1!=10)
								{
									try {
										tweetsResult1 = twitterInstance.search(twitterQuery1);


									} catch (TwitterException e) {
										e.printStackTrace();
									}
									tweetsTen = tweetsResult1.getTweets();
								}

								ArrayNode jsonObj = Json.newArray();
								tweetsTen.forEach((tweet) -> {
									tweetlist.add(tweet);
									ObjectNode node = Json.newObject();
									node.put("userName", tweet.getUser().getName());
									node.put("userScreenName", tweet.getUser().getScreenName());
									try {
										node.put("tweetLocation", tweet.getPlace().getFullName());
									}catch(NullPointerException e)
									{
										node.put("tweetLocation", "");
									}
									node.put("textbody",tweet.getText());
									HashtagEntity[] hashtags = tweet.getHashtagEntities();
									node.put("length",hashtags.length+"");
									if (hashtags != null) {
										for (int i=0; i<hashtags.length; i++) {
											String key = "hashtag" + i;
											node.put(key,"#"+hashtags[i].getText());
										}}

									node.put("sentiment", emotion);
									String x = searchKeys.replaceAll("[^a-zA-Z]+", "");
									if(searchKeys.equals(":)")||searchKeys.equals(":-)"))
										x="ppppp";
									if(searchKeys.equals(":(")||searchKeys.equals(":-("))
										x="qqqq";
									node.put("searchKeys", "div" + x);
									jsonObj.add(node);
								});
								ac.tell(jsonObj.toString(), self()) ;

							}

							if(flag==1)
							{
								Query twitterQuery1 = new Query(searchKeys);
								twitterQuery1.setCount(10);
								QueryResult tweetsResult1 = null;
								try {
									tweetsResult1 = twitterInstance.search(twitterQuery1);

								} catch (TwitterException e) {
									e.printStackTrace();
								}
								List<Status> tweetsNew = tweetsResult1.getTweets();

								tweetsNew.forEach((tweet) -> {
									if(!tweetlist.contains(tweet))
									{
										ArrayNode jsonObj = Json.newArray();

										tweetlist.add(tweet);
										ObjectNode node = Json.newObject();
										node.put("userName", tweet.getUser().getName());
										node.put("userScreenName", tweet.getUser().getScreenName());
										try {
											node.put("tweetLocation", tweet.getPlace().getFullName());
										}catch(NullPointerException e)
										{
											node.put("tweetLocation", "");
										}
										node.put("textbody",tweet.getText());
										HashtagEntity[] hashtags = tweet.getHashtagEntities();
										node.put("length",hashtags.length+"");
										if (hashtags != null) {
											for (int i=0; i<hashtags.length; i++) {
												String key = "hashtag" + i;
												node.put(key,"#"+hashtags[i].getText());
											}}

										node.put("sentiment", emotion);
										String x = searchKeys.replaceAll("[^a-zA-Z]+", "");
										if(searchKeys.equals(":)")||searchKeys.equals(":-)"))
											x="ppppp";
										if(searchKeys.equals(":(")||searchKeys.equals(":-("))
											x="qqqq";
										node.put("searchKeys", "div" + x);
										jsonObj.add(node);
										ac.tell(jsonObj.toString(), self()) ;}

								});
							}
						}
					};

					ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
					service.scheduleWithFixedDelay(r, 0, 10, TimeUnit.SECONDS);
				}).build();
	}


}

