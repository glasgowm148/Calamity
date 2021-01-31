package services;

import akka.actor.AbstractActor;
import akka.actor.Props;



/**
 * @version 1.0
 * @see sentimentActor

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
     *

    //@Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Sentiment.class, info -> sender().tell(jsonReader.parseEvent(info.name),self())).build();
    }
}
 */
// tweet.getPositiveSentiment()