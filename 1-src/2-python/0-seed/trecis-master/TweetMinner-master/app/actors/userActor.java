package actors;

import services.ConnectTwitter;
import akka.actor.AbstractActor;
import akka.actor.Props;

/**
 * userActor class calls the getProfileInfo method of Connect Twitter class to
 * fetch profile info and ten latest tweets for the username hyperlink clicked.
 * @version 1.0
 * @see userActor
 * @author Anumeet
 */
public class userActor extends AbstractActor {

    public static Props getProps() {
        return Props.create(userActor.class);
    }

    public static class User {
        public final String name;

        public User(String name) {
            this.name = name;
        }
    }

    /**
     * createRecieve method for userActor that  calls the getProfileInfo method of Connect Twitter class to
     * fetch profile infor and ten latest tweets for the username hyperlink clicked.
     * @author Anumeet
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(User.class, info -> {
                    sender().tell(ConnectTwitter.getProfileInfo(info.name),self());
                }).build();
    }
}
