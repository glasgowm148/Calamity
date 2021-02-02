package actors;

import services.ConnectTwitter;
import akka.actor.AbstractActor;
import akka.actor.Props;

/**
 * tweetswordsActor class calls the tweetwords method of Connect Twitter class to
 * fetch the list of distinct words and their frequencies.
 * @version 1.0
 * @see tweetsWordsActor
 * @author Komaldeep Singh
 */
public class tweetsWordsActor extends AbstractActor {

    public static Props getProps() {
        return Props.create(tweetsWordsActor.class);
    }

    public static class SearchTerms {
        public final String name;

        public SearchTerms(String name) {
            this.name = name;
        }
    }

    /**
     * createRecieve method for tweetsWordsActor that  calls the tweetWords method of Connect Twitter class to
     * fetch the list of distinct words and their frequencies.
     * @author Komaldeep Singh
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SearchTerms.class, info -> {
                    sender().tell(ConnectTwitter.tweetWords(info.name),self());
                }).build();
    }
}