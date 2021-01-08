package actors;

import services.ConnectTwitter;
import akka.actor.AbstractActor;
import akka.actor.Props;

/**
 * hashtagActor class calls the getHashtagTweets method of Connect Twitter class to
 * fetch ten latest tweets for the hashtag hyperlink clicked.
 * @version 1.0
 * @see hashtagActor
 * @author Navdeep Kaur Brar
 */
public class hashtagActor extends AbstractActor {

    public static Props getProps() {
        return Props.create(hashtagActor.class);
    }

    public static class Hashtag {
        public final String name;

        public Hashtag(String name) {
            this.name = name;
        }
    }

    /**
     * createRecieve method for hashtagActor that  calls the getHashtagTweets method of Connect Twitter class to
     * fetch ten latest tweets for the hashtag hyperlink clicked.
     * @author Navdeep Kaur Brar
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Hashtag.class, info -> {
                    sender().tell(ConnectTwitter.getHashtagTweets(info.name),self());
                }).build();
    }
}

