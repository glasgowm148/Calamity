package actors;

import services.ConnectTwitter;
import akka.actor.AbstractActor;
import akka.actor.Props;

/**
 * locationActor class calls the getLocationTweets method of Connect Twitter class to
 * fetch ten latest tweets for the location hyperlink clicked.
 * @version 1.0
 * @see locationActor
 * @author Ishdeep
 */
public class locationActor extends AbstractActor {

    public static Props getProps() {
        return Props.create(locationActor.class);
    }

    public static class Location {
        public final String name;

        public Location(String name) {
            this.name = name;
        }
    }
    /**
     * createRecieve method for locationActor that  calls the getLocationTweets method of Connect Twitter class to
     * fetch ten latest tweets for the location hyperlink clicked.
     * @author Ishdeep
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Location.class, info -> {
                    sender().tell(ConnectTwitter.getLocationTweets(info.name),self());
                }).build();
    }
}
