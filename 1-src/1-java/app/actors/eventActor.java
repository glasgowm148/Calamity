package actors;

import akka.actor.AbstractActor;
import akka.actor.Props;



/**
 * @version 1.0
 * @see eventActor
 */
public class eventActor extends AbstractActor {

    public static final String ACTOR_NAME = "event_actor";

    public static Props getProps() {
        return Props.create(sentimentActor.class);
    }

    public static class Event {
        public final String name;

        public Event(String name) {
            this.name = name;
        }
    }

    /**
     *
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Event.class, info -> sender().tell(jsonReader.parseEvent(info.name),self())).build();
    }

    public static class parse {

        public parse(String name) {
            System.out.println("======");
            System.out.println("======");
            System.out.println(name);
            System.out.println("======");
            System.out.println("======");
        }
    }
}

// tweet.getPositiveSentiment()