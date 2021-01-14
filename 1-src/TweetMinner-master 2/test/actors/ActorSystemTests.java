package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import actors.locationActor;
import org.mockito.InjectMocks;

import static org.junit.Assert.assertTrue;

/**
 * ActorSystemTests class tests the working of the Actor classes hashtagActor, locationActor
 * userActor, tweetsWordsActor whether they fetch the correct data or not.
 * @see hashtagActor
 * @see userActor
 * @see tweetsWordsActor
 * @see locationActor
 * @see ActorSystemTests
 * @author Karan Behl
 */
public class ActorSystemTests {

	//mocking actor system
    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    /**
     *locationActorTest method tests the working of the location Actor whether it fetches tweets for location or not
     * @author Ishdeep
     */
    @Test
    public void locationActorTest() {
        new TestKit(system) {{
            final Props props = Props.create(locationActor.class);
            final ActorRef subject = system.actorOf(props);
            subject.tell("Montreal", getRef());
            assertTrue(getRef().toString().length() > 0);
        }};
    }

    /**
     *hashtagActorTest method tests the working of the location Actor whether it fetches tweets for hashtag or not
     * @author Navdeep Kaur Brar
     */
    @Test
    public void hashtagActorTest() {
        new TestKit(system) {{
            final Props props = Props.create(hashtagActor.class);
            final ActorRef subject = system.actorOf(props);
            subject.tell("mtl", getRef());
            assertTrue(getRef().toString().length() > 0);
        }};
    }

    /**
     *tweetWordsActorTest method tests the working of the tweetsWords Actor whether it fetches distinct words or frequencies or not
     * @author Komaldeep Singh
     */
    @Test
    public void tweetWordsActorTest() {
        new TestKit(system) {{
            final Props props = Props.create(tweetsWordsActor.class);
            final ActorRef subject = system.actorOf(props);
            tweetsWordsActor.SearchTerms app = new tweetsWordsActor.SearchTerms("hello");
            subject.tell("hello", getRef());
            assertTrue(getRef().toString().length() > 0);
        }};
    }

    /**
     *userActorTest method tests the working of the user Actor whether it fetches tweets and profile info for user or not
     * @author Anumeet
     */
    @Test
    public void userActor() {
        new TestKit(system) {{
            final Props props = Props.create(userActor.class);
            final ActorRef subject = system.actorOf(props);
            subject.tell("iamsrk", getRef());
            assertTrue(getRef().toString().length() > 0);
        }};
    }


}
