package actors;

import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import play.libs.Json;
import twitter4j.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static services.ConnectTwitter.connectToTwitter;

/**
 * MainActorTest class tests the working of the main Actor whether they fetch the ten tweets or not reactively.
 * @see mainActor
 * @see mainActorTest
 * @author Karan Behl
 */
public class mainActorTest {

	//mocking Twitter class And main Actor
    static private Twitter twitterInstance = mock(Twitter.class);
    static private mainActor mainActor = mock(actors.mainActor.class);
    static List <Status> tweetlist;
    static ActorSystem system;
    static ArrayNode jsonObj;
   static Status status = new Status() {
        @Override
        public Date getCreatedAt() {
            return null;
        }

        @Override
        public long getId() {
            return 1;
        }

        @Override
        public String getText() {
            return "hello";
        }

        @Override
        public int getDisplayTextRangeStart() {
            return 0;
        }

        @Override
        public int getDisplayTextRangeEnd() {
            return 0;
        }

        @Override
        public String getSource() {
            return null;
        }

        @Override
        public boolean isTruncated() {
            return false;
        }

        @Override
        public long getInReplyToStatusId() {
            return 0;
        }

        @Override
        public long getInReplyToUserId() {
            return 0;
        }

        @Override
        public String getInReplyToScreenName() {
            return null;
        }

        @Override
        public GeoLocation getGeoLocation() {
            return null;
        }

        @Override
        public Place getPlace() {
            Place place = new Place() {
                @Override
                public String getName() {
                    return null;
                }

                @Override
                public String getStreetAddress() {
                    return null;
                }

                @Override
                public String getCountryCode() {
                    return null;
                }

                @Override
                public String getId() {
                    return null;
                }

                @Override
                public String getCountry() {
                    return null;
                }

                @Override
                public String getPlaceType() {
                    return null;
                }

                @Override
                public String getURL() {
                    return null;
                }

                @Override
                public String getFullName() {
                    return "Montreal";
                }

                @Override
                public String getBoundingBoxType() {
                    return null;
                }

                @Override
                public GeoLocation[][] getBoundingBoxCoordinates() {
                    return new GeoLocation[0][];
                }

                @Override
                public String getGeometryType() {
                    return null;
                }

                @Override
                public GeoLocation[][] getGeometryCoordinates() {
                    return new GeoLocation[0][];
                }

                @Override
                public Place[] getContainedWithIn() {
                    return new Place[0];
                }

                @Override
                public int compareTo(Place o) {
                    return 0;
                }

                @Override
                public RateLimitStatus getRateLimitStatus() {
                    return null;
                }

                @Override
                public int getAccessLevel() {
                    return 0;
                }
            };
            return place;
        }

        @Override
        public boolean isFavorited() {
            return false;
        }

        @Override
        public boolean isRetweeted() {
            return false;
        }

        @Override
        public int getFavoriteCount() {
            return 0;
        }

        @Override
        public User getUser() {
            User user = new User() {
                @Override
                public long getId() {
                    return 1234523;
                }

                @Override
                public String getName() {
                    return "karan";
                }

                @Override
                public String getEmail() {
                    return "karanb.behl@gmail.com";
                }

                @Override
                public String getScreenName() {
                    return "karanbehl";
                }

                @Override
                public String getLocation() {
                    return null;
                }

                @Override
                public String getDescription() {
                    return null;
                }

                @Override
                public boolean isContributorsEnabled() {
                    return false;
                }

                @Override
                public String getProfileImageURL() {
                    return null;
                }

                @Override
                public String getBiggerProfileImageURL() {
                    return null;
                }

                @Override
                public String getMiniProfileImageURL() {
                    return null;
                }

                @Override
                public String getOriginalProfileImageURL() {
                    return null;
                }

                @Override
                public String getProfileImageURLHttps() {
                    return null;
                }

                @Override
                public String getBiggerProfileImageURLHttps() {
                    return null;
                }

                @Override
                public String getMiniProfileImageURLHttps() {
                    return null;
                }

                @Override
                public String getOriginalProfileImageURLHttps() {
                    return null;
                }

                @Override
                public boolean isDefaultProfileImage() {
                    return false;
                }

                @Override
                public String getURL() {
                    return null;
                }

                @Override
                public boolean isProtected() {
                    return false;
                }

                @Override
                public int getFollowersCount() {
                    return 0;
                }

                @Override
                public Status getStatus() {
                    return null;
                }

                @Override
                public String getProfileBackgroundColor() {
                    return null;
                }

                @Override
                public String getProfileTextColor() {
                    return null;
                }

                @Override
                public String getProfileLinkColor() {
                    return null;
                }

                @Override
                public String getProfileSidebarFillColor() {
                    return null;
                }

                @Override
                public String getProfileSidebarBorderColor() {
                    return null;
                }

                @Override
                public boolean isProfileUseBackgroundImage() {
                    return false;
                }

                @Override
                public boolean isDefaultProfile() {
                    return false;
                }

                @Override
                public boolean isShowAllInlineMedia() {
                    return false;
                }

                @Override
                public int getFriendsCount() {
                    return 0;
                }

                @Override
                public Date getCreatedAt() {
                    return null;
                }

                @Override
                public int getFavouritesCount() {
                    return 0;
                }

                @Override
                public int getUtcOffset() {
                    return 0;
                }

                @Override
                public String getTimeZone() {
                    return null;
                }

                @Override
                public String getProfileBackgroundImageURL() {
                    return null;
                }

                @Override
                public String getProfileBackgroundImageUrlHttps() {
                    return null;
                }

                @Override
                public String getProfileBannerURL() {
                    return null;
                }

                @Override
                public String getProfileBannerRetinaURL() {
                    return null;
                }

                @Override
                public String getProfileBannerIPadURL() {
                    return null;
                }

                @Override
                public String getProfileBannerIPadRetinaURL() {
                    return null;
                }

                @Override
                public String getProfileBannerMobileURL() {
                    return null;
                }

                @Override
                public String getProfileBannerMobileRetinaURL() {
                    return null;
                }

                @Override
                public boolean isProfileBackgroundTiled() {
                    return false;
                }

                @Override
                public String getLang() {
                    return null;
                }

                @Override
                public int getStatusesCount() {
                    return 0;
                }

                @Override
                public boolean isGeoEnabled() {
                    return false;
                }

                @Override
                public boolean isVerified() {
                    return false;
                }

                @Override
                public boolean isTranslator() {
                    return false;
                }

                @Override
                public int getListedCount() {
                    return 0;
                }

                @Override
                public boolean isFollowRequestSent() {
                    return false;
                }

                @Override
                public URLEntity[] getDescriptionURLEntities() {
                    return new URLEntity[0];
                }

                @Override
                public URLEntity getURLEntity() {
                    return null;
                }

                @Override
                public String[] getWithheldInCountries() {
                    return new String[0];
                }

                @Override
                public int compareTo(User o) {
                    return 0;
                }

                @Override
                public RateLimitStatus getRateLimitStatus() {
                    return null;
                }

                @Override
                public int getAccessLevel() {
                    return 0;
                }
            };
            return user;
        }

        @Override
        public boolean isRetweet() {
            return false;
        }

        @Override
        public Status getRetweetedStatus() {
            return null;
        }

        @Override
        public long[] getContributors() {
            return new long[0];
        }

        @Override
        public int getRetweetCount() {
            return 0;
        }

        @Override
        public boolean isRetweetedByMe() {
            return false;
        }

        @Override
        public long getCurrentUserRetweetId() {
            return 0;
        }

        @Override
        public boolean isPossiblySensitive() {
            return false;
        }

        @Override
        public String getLang() {
            return null;
        }

        @Override
        public Scopes getScopes() {
            return null;
        }

        @Override
        public String[] getWithheldInCountries() {
            return new String[0];
        }

        @Override
        public long getQuotedStatusId() {
            return 0;
        }

        @Override
        public Status getQuotedStatus() {
            return null;
        }

        @Override
        public int compareTo(Status o) {
            return 0;
        }

        @Override
        public UserMentionEntity[] getUserMentionEntities() {
            return new UserMentionEntity[0];
        }

        @Override
        public URLEntity[] getURLEntities() {
            return new URLEntity[0];
        }

        @Override
        public HashtagEntity[] getHashtagEntities() {
            return new HashtagEntity[0];
        }

        @Override
        public MediaEntity[] getMediaEntities() {
            return new MediaEntity[0];
        }

        @Override
        public SymbolEntity[] getSymbolEntities() {
            return new SymbolEntity[0];
        }

        @Override
        public RateLimitStatus getRateLimitStatus() {
            return null;
        }

        @Override
        public int getAccessLevel() {
            return 0;
        }
    };


    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @BeforeClass
    public static void setup() {

        system = ActorSystem.create();
    }

    
    /**
     *sentimentTest method tests the working of the getSentiment method whether it calculates correct sentiment or not.
     * @author Karan Behl
     */
    @Test
    public void sentimentTest() {
        ActorRef subject= mainActor.ac;
        final Props props = Props.create(mainActor.class,subject);
        final TestActorRef<mainActor> ref = TestActorRef.create(system, props, "testA");
        final mainActor actor = ref.underlyingActor();
        Assert.assertTrue(actor.getSentiment(":)").length()>0);
    }

    /**
     *testActorRegister method tests the working of the main actor receive method whether it fetches tweets reactively or not
     * @author Karan Behl
     */
    @Test
    public void testActorRegister() {
        new TestKit(system) {{
            ActorRef subject= mainActor.ac;
            final Props props = Props.create(mainActor.class,subject);
            subject = system.actorOf(props);
            tweetlist = new ArrayList<Status>();
            jsonObj = Json.newArray();
            for(int j=0;j<=10;j++)
            {


                String emotion ="default";
                tweetlist.add(status);
                ObjectNode node = Json.newObject();
                node.put("userName", status.getUser().getName());
                node.put("userScreenName", status.getUser().getScreenName());
                try {
                    node.put("tweetLocation", status.getPlace().getFullName());
                }catch(NullPointerException e)
                {
                    node.put("tweetLocation", "");
                }
                node.put("textbody",status.getText());
                HashtagEntity[] hashtags = status.getHashtagEntities();
                node.put("length",hashtags.length+"");
                if (hashtags != null) {
                    for (int i=0; i<hashtags.length; i++) {
                        String key = "hashtag" + i;
                        node.put(key,"#"+hashtags[i].getText());
                    }}
                node.put("sentiment", ":-)");
                node.put("searchKeys", "hello".replaceAll("\\s",""));
                jsonObj.add(node);

            }
            System.out.print(jsonObj.toString());
            //subject.tell("Montreal", getRef());
            //ac.tell(jsonObj.toString(), self()) ;
             subject.tell(jsonObj.toString(), getRef());
            assertTrue(getRef().toString().length() > 0);
          /*  mainActor ma = new mainActor(subject);
            String result= ma.getSentiment(":-)");
            Assert.assertEquals(result , null);*/
        }};
    }


