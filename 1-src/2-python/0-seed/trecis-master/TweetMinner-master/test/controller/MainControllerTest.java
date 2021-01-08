package controller;

import akka.actor.ActorSystem;
import akka.stream.Materializer;
import controllers.HomeController;
import controllers.MainController;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import actors.mainActor;
import actors.mainActorTest;
import play.api.http.MediaRange;
import play.api.mvc.RequestHeader;
import play.i18n.Lang;
import play.libs.typedmap.TypedKey;
import play.libs.typedmap.TypedMap;
import play.mvc.Controller;
import play.mvc.Http;

import java.security.cert.X509Certificate;
import java.util.*;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
/**
 * MainControllerTest class tests the working of the MainController whether it handles the web socket request properly 
 * or not and creates a connection between main actor and client
 * @see MainController
 * @see MainControllerTest
 * @author Karan Behl
 */
public class MainControllerTest extends Controller {
	
	//mocking the mainController
    private ActorSystem actorSystem;
    private Materializer materializer;
    @InjectMocks
    MainController app = new MainController(actorSystem,materializer);


    /**
     *testmaincontroller tests whether the mainController is working properly and creating a web socket connection between client and mainActor or not
     * @author Navdeep Kaur Brar
     */
    @Test
    public void testmaincontroller() throws Exception{
        MockitoAnnotations.initMocks( this );
        assertTrue(app!=null);
        Assert.assertTrue(app.ws()!=null  );
        Assert.assertEquals(app.originMatches("localhost:9000"),true);
        Http.RequestHeader http = new Http.RequestHeader() {
            @Override
            public String uri() {
                return "www.twitter.com";
            }

            @Override
            public String method() {
                return "method";
            }

            @Override
            public String version() {
                return "Origin";
            }

            @Override
            public String remoteAddress() {
                return "localhost:9000";
            }

            @Override
            public boolean secure() {
                return false;
            }

            @Override
            public TypedMap attrs() {
                return null;
            }

            @Override
            public Http.RequestHeader withAttrs(TypedMap newAttrs) {
                return null;
            }

            @Override
            public <A> Http.RequestHeader addAttr(TypedKey<A> key, A value) {
                return null;
            }

            @Override
            public Http.Request withBody(Http.RequestBody body) {
                return null;
            }

            @Override
            public String host() {
                return null;
            }

            @Override
            public String path() {
                return null;
            }

            @Override
            public List<Lang> acceptLanguages() {
                return null;
            }

            @Override
            public List<MediaRange> acceptedTypes() {
                return null;
            }

            @Override
            public boolean accepts(String mimeType) {
                return false;
            }

            @Override
            public Map<String, String[]> queryString() {
                return null;
            }

            @Override
            public String getQueryString(String key) {
                return null;
            }

            @Override
            public Http.Cookies cookies() {
                return null;
            }

            @Override
            public Http.Cookie cookie(String name) {
                return null;
            }

            @Override
            public Http.Headers getHeaders() {
                Http.Headers headers = new Http.Headers(createMap());
                return headers;
            }
            public Map<String, List<String>> createMap()
            {
                Map<String,List<String>> myMap = new HashMap<String,List<String>>();
                List<String> supplierNames = Arrays.asList("Origin", "sup2", "sup3");
                myMap.put("Origin", supplierNames);
                return myMap;
            }

            @Override
            public boolean hasBody() {
                return false;
            }

            @Override
            public Optional<String> contentType() {
                return Optional.empty();
            }

            @Override
            public Optional<String> charset() {
                return Optional.empty();
            }

            @Override
            public Optional<List<X509Certificate>> clientCertificateChain() {
                return Optional.empty();
            }

            @Override
            public Map<String, String> tags() {
                return null;
            }

            @Override
            public RequestHeader _underlyingHeader() {
                return null;
            }

            @Override
            public RequestHeader asScala() {
                return null;
            }
        };
        Assert.assertFalse(app.sameOriginCheck(http));
    }

}
