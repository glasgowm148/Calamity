package controllers;

import play.mvc.*;
import play.libs.F;
import play.libs.streams.ActorFlow;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import actors.mainActor;
import akka.actor.*;
import akka.stream.*;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.slf4j.Logger;

/**
 * mainController class contains a main method to handle the web socket request
 * from the front end establish a web socket connection to reactively search for tweets and display. The main actor is called
 * via the main controller to search and display tweets.
 * @version 1.0
 * @see MainController
 * @author Navdeep Kaur Brar
 */
@Singleton
public class MainController extends Controller {

	private final Logger logger = org.slf4j.LoggerFactory.getLogger("controllers.MainController");
	private final ActorSystem actor_system;
	private final Materializer materializer;

	//dependency injection to initalize the Actor System
	@Inject
	public MainController(ActorSystem actor_system, Materializer materializer) {
		this.actor_system = actor_system;
		this.materializer = materializer;
	}

	/**
	 * main method opens a Web Socket connection to continuously call the main actor to search for tweets and display those.
	 * @author Navdeep Kaur Brar
	 * 
	 */
	public WebSocket main() {

		return WebSocket.Text.acceptOrResult(request -> {
			if (sameOriginCheck(request)) {
				return CompletableFuture.completedFuture(
						F.Either.Right(ActorFlow.actorRef(mainActor::props,
								actor_system, materializer)));
			} else {
				return CompletableFuture.completedFuture(F.Either.Left(forbidden()));
			}
		});
	}

	/**
	 * Checks that the WebSocket comes from the same origin.  This is necessary to protect
	 * against Cross-Site WebSocket Hijacking as WebSocket does not implement Same Origin Policy.
	 * <p>
	 * See https://tools.ietf.org/html/rfc6455#section-1.3 and
	 * http://blog.dewhurstsecurity.com/2013/08/30/security-testing-html5-websockets.html
	 */
	public boolean sameOriginCheck(Http.RequestHeader rh) {
		final Optional<String> origin = rh.header("Origin");

		if (! origin.isPresent()) {
			logger.error("originCheck: rejecting request because no Origin header found");
			return false;
		} else if (originMatches(origin.get())) {
			logger.debug("originCheck: originValue = " + origin);
			return true;
		} else {
			logger.error("originCheck: rejecting request because Origin header value " + origin + " is not in the same origin");
			return false;
		}
	}

	public boolean originMatches(String origin) {
		return origin.contains("localhost:9000") || origin.contains("localhost:19001");
	}
}
