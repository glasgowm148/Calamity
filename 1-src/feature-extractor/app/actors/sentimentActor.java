package actors;

import services.ConnectTwitter;
import akka.actor.AbstractActor;
import akka.actor.Props;

/**
 * sentimentActor class calls the getSentiment method of Connect Twitter class to
 * calculate the sentiment associated with the searched tweets.
 * @version 1.0
 * @see sentimentActor
 * @author Karan Behl
 */
public class sentimentActor extends AbstractActor {

	public static Props getProps() {
		return Props.create(sentimentActor.class);
	}

	public static class Sentiment {
		public final String name;

		public Sentiment(String name) {
			this.name = name;
		}
	}

	/**
	 * createRecieve method for sentimentActor that  calls the getSentiment method of Connect Twitter class to
	 * calculate the sentiment associated with the searched tweets.
	 * @author Karan Behl
	 */
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(Sentiment.class, info -> {
					sender().tell(ConnectTwitter.getSentiment(info.name),self());
				}).build();
	}
}