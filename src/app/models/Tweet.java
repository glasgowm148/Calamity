
package models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fforbeck on 24/01/15.
 *
 */
//@Entity
@Parcel
public class Tweet {
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("id")
        @Expose
        private Long id;
        @SerializedName("id_str")
        @Expose
        private String idStr;
        @SerializedName("text")
        @Expose
        private String text;
        @SerializedName("truncated")
        @Expose
        private Boolean truncated;
        @SerializedName("entities")
        @Expose
        private Entities entities;
        @SerializedName("user")
        @Expose
        private User user;
        @SerializedName("is_quote_status")
        @Expose
        private Boolean isQuoteStatus;
        @SerializedName("retweet_count")
        @Expose
        private Integer retweetCount;
        @SerializedName("favorite_count")
        @Expose
        private Integer favoriteCount;
        @SerializedName("favorited")
        @Expose
        private Boolean favorited;
        @SerializedName("retweeted")
        @Expose
        private Boolean retweeted;
        @SerializedName("retweeted_status")
        @Expose
        private Retweet retweet;

        public String getCreatedAt() {
            return createdAt;
        }

        public Long getId() {
            return id;
        }

        public String getIdStr() {
            return idStr;
        }

        public String getText() {
            return text;
        }

        public Boolean getTruncated() {
            return truncated;
        }

        public Entities getEntities() {
            return entities;
        }

        public User getUser() {
            return user;
        }

        public Boolean getQuoteStatus() {
            return isQuoteStatus;
        }

        public Integer getRetweetCount() {
            return retweetCount;
        }

        public Integer getFavoriteCount() {
            return favoriteCount;
        }

        public Boolean getFavorited() {
            return favorited;
        }

        public Boolean getRetweeted() {
            return retweeted;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setIdStr(String idStr) {
            this.idStr = idStr;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setTruncated(Boolean truncated) {
            this.truncated = truncated;
        }

        public void setEntities(Entities entities) {
            this.entities = entities;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public void setQuoteStatus(Boolean quoteStatus) {
            isQuoteStatus = quoteStatus;
        }

        public void setRetweetCount(Integer retweetCount) {
            this.retweetCount = retweetCount;
        }

        public void setFavoriteCount(Integer favoriteCount) {
            this.favoriteCount = favoriteCount;
        }

        public void setFavorited(Boolean favorited) {
            this.favorited = favorited;
        }

        public void setRetweeted(Boolean retweeted) {
            this.retweeted = retweeted;
        }

        public Retweet getRetweet() {
            return retweet;
        }

        public void setRetweet(Retweet retweet) {
            this.retweet = retweet;
        }


    final long serialVersionUID = 42l;
    final ObjectMapper mapper = new ObjectMapper();
    String jsonText = null;
    private Map<String,String> properties = null;


    public Tweet(JsonNode json) throws JsonProcessingException {

        jsonText = json.toString();
        ObjectMapper mapper = new ObjectMapper();
        //JsonNode tweetJsonNode = mapper.readTree(String.valueOf(json));
        JsonNode jsonNode1 = json.get("full_text");

        Tweet tweet = mapper.convertValue(json, Tweet.class);
        System.out.println(tweet);
        //assertThat(jsonNode1.textValue(), equalTo("v1"));

       // doParsing(tweet);


        if (json.get("lang") != null && "en".equals(json.get("lang").textValue())) {
            if (json.get("id") != null && json.get("full_text") != null) {
                this.id = json.get("id").longValue();
                this.text = json.get("full_text").textValue();
                this.createdAt = json.get("created_at").textValue();


            }
        }
        System.out.println(id);
        System.out.println(text);
        System.out.println(createdAt);
        System.out.println(json);





    }

    public void doParsing(JsonObject json) {

        if (json.get("created_at") != null)
            if (!json.get("created_at").isJsonNull()) addProperty("created_at", json.get("created_at").getAsString());
        if (json.get("source") != null)
            if (!json.get("source").isJsonNull()) addProperty("source", json.get("source").getAsString());
        if (json.get("lang") != null)
            if (!json.get("lang").isJsonNull()) addProperty("lang", json.get("lang").getAsString());
        if (json.get("text") != null)
            if (!json.get("text").isJsonNull()) addProperty("text", json.get("text").getAsString());
    }

    private void addProperty(String key, String value) {

    }
    /**
    public Tuple2<Long, String> call(String json) {
        try {
            JsonNode root = mapper.readValue(json, JsonNode.class);
            long id;
            String text;
            if (root.get("lang") != null && "en".equals(root.get("lang").textValue())) {
                if (root.get("id") != null && root.get("text") != null) {
                    id = root.get("id").longValue();
                    text = root.get("text").textValue();
                    return new Tuple2<Long, String>(id, text);
                }
                return null;
            }
            return null;
        } catch (IOException ex) {
            Logger LOG = Logger.getLogger(String.valueOf(this.getClass()));

        }
        return null;
    }

    @Override
    public String toString() {
        return Objects.toStringhelper(this)
                .add("id", id)
                .add("user_id", userId)
                .add("user_name", userName)
                .add("text", text)
                .add("hash_tag", hashTag)
                .add("lang", lang)
                .add("sentiment", sentiment)
                .add("sentiment_score", sentimentScore)
                .add("created_at", createdAt)
                .add("retweets", retweets)
                .toString();
    }
    */

}
@Parcel
class Entities {

    @SerializedName("media")
    @Expose
    private List<Medium> media = new ArrayList<Medium>();

    public List<Medium> getMedia() {
        return media;
    }

    public void setMedia(List<Medium> media) {
        this.media = media;
    }
}

@Parcel
class Retweet {

    @SerializedName("user")
    @Expose
    private User user;

    @SerializedName("favorite_count")
    @Expose
    private Integer favoriteCount;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(Integer favoriteCount) {
        this.favoriteCount = favoriteCount;
    }
}

@Parcel
class User {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("id_str")
    @Expose
    private String idStr;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("screen_name")
    @Expose
    private String screenName;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("followers_count")
    @Expose
    private Integer followersCount;
    @SerializedName("friends_count")
    @Expose
    private Integer friendsCount;
    @SerializedName("listed_count")
    @Expose
    private Integer listedCount;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("favourites_count")
    @Expose
    private Integer favouritesCount;
    @SerializedName("utc_offset")
    @Expose
    private Integer utcOffset;
    @SerializedName("time_zone")
    @Expose
    private String timeZone;
    @SerializedName("geo_enabled")
    @Expose
    private Boolean geoEnabled;
    @SerializedName("verified")
    @Expose
    private Boolean verified;
    @SerializedName("statuses_count")
    @Expose
    private Integer statusesCount;
    @SerializedName("lang")
    @Expose
    private String profileBackgroundColor;
    @SerializedName("profile_background_image_url")
    @Expose
    private String profileBackgroundImageUrl;
    @SerializedName("profile_background_image_url_https")
    @Expose
    private String profileBackgroundImageUrlHttps;
    @SerializedName("profile_background_tile")
    @Expose
    private Boolean profileBackgroundTile;
    @SerializedName("profile_image_url")
    @Expose
    private String profileImageUrl;
    @SerializedName("profile_image_url_https")
    @Expose
    private String profileImageUrlHttps;
    @SerializedName("profile_banner_url")
    @Expose
    private String profileBannerUrl;
    @SerializedName("profile_link_color")
    @Expose
    private String profileLinkColor;
    @SerializedName("profile_sidebar_border_color")
    @Expose
    private String profileSidebarBorderColor;
    @SerializedName("profile_sidebar_fill_color")
    @Expose
    private String profileSidebarFillColor;
    @SerializedName("profile_text_color")
    @Expose
    private String profileTextColor;
    @SerializedName("profile_use_background_image")
    @Expose
    private Boolean profileUseBackgroundImage;
    @SerializedName("has_extended_profile")
    @Expose
    private Boolean hasExtendedProfile;
    @SerializedName("default_profile")
    @Expose
    private Boolean defaultProfile;
    @SerializedName("default_profile_image")
    @Expose
    private Boolean defaultProfileImage;
    @SerializedName("following")
    @Expose
    private Boolean following;
    @SerializedName("follow_request_sent")
    @Expose
    private Boolean followRequestSent;
    @SerializedName("notifications")
    @Expose
    private Boolean notifications;
    @SerializedName("translator_type")
    @Expose
    private String translatorType;

    public Long getId() {
        return id;
    }

    public String getIdStr() {
        return idStr;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public Integer getFollowersCount() {
        return followersCount;
    }

    public Integer getFriendsCount() {
        return friendsCount;
    }

    public Integer getListedCount() {
        return listedCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Integer getFavouritesCount() {
        return favouritesCount;
    }

    public Integer getUtcOffset() {
        return utcOffset;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public Boolean getGeoEnabled() {
        return geoEnabled;
    }

    public Boolean getVerified() {
        return verified;
    }

    public Integer getStatusesCount() {
        return statusesCount;
    }

    public String getProfileBackgroundColor() {
        return profileBackgroundColor;
    }

    public String getProfileBackgroundImageUrl() {
        return profileBackgroundImageUrl;
    }

    public String getProfileBackgroundImageUrlHttps() {
        return profileBackgroundImageUrlHttps;
    }

    public Boolean getProfileBackgroundTile() {
        return profileBackgroundTile;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getProfileImageUrlHttps() {
        return profileImageUrlHttps;
    }

    public String getProfileBannerUrl() {
        return profileBannerUrl;
    }

    public String getProfileLinkColor() {
        return profileLinkColor;
    }

    public String getProfileSidebarBorderColor() {
        return profileSidebarBorderColor;
    }

    public String getProfileSidebarFillColor() {
        return profileSidebarFillColor;
    }

    public String getProfileTextColor() {
        return profileTextColor;
    }

    public Boolean getProfileUseBackgroundImage() {
        return profileUseBackgroundImage;
    }

    public Boolean getHasExtendedProfile() {
        return hasExtendedProfile;
    }

    public Boolean getDefaultProfile() {
        return defaultProfile;
    }

    public Boolean getDefaultProfileImage() {
        return defaultProfileImage;
    }

    public Boolean getFollowing() {
        return following;
    }

    public Boolean getFollowRequestSent() {
        return followRequestSent;
    }

    public Boolean getNotifications() {
        return notifications;
    }

    public String getTranslatorType() {
        return translatorType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    public void setFriendsCount(Integer friendsCount) {
        this.friendsCount = friendsCount;
    }

    public void setListedCount(Integer listedCount) {
        this.listedCount = listedCount;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setFavouritesCount(Integer favouritesCount) {
        this.favouritesCount = favouritesCount;
    }

    public void setUtcOffset(Integer utcOffset) {
        this.utcOffset = utcOffset;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public void setGeoEnabled(Boolean geoEnabled) {
        this.geoEnabled = geoEnabled;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public void setStatusesCount(Integer statusesCount) {
        this.statusesCount = statusesCount;
    }

    public void setProfileBackgroundColor(String profileBackgroundColor) {
        this.profileBackgroundColor = profileBackgroundColor;
    }

    public void setProfileBackgroundImageUrl(String profileBackgroundImageUrl) {
        this.profileBackgroundImageUrl = profileBackgroundImageUrl;
    }

    public void setProfileBackgroundImageUrlHttps(String profileBackgroundImageUrlHttps) {
        this.profileBackgroundImageUrlHttps = profileBackgroundImageUrlHttps;
    }

    public void setProfileBackgroundTile(Boolean profileBackgroundTile) {
        this.profileBackgroundTile = profileBackgroundTile;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setProfileImageUrlHttps(String profileImageUrlHttps) {
        this.profileImageUrlHttps = profileImageUrlHttps;
    }

    public void setProfileBannerUrl(String profileBannerUrl) {
        this.profileBannerUrl = profileBannerUrl;
    }

    public void setProfileLinkColor(String profileLinkColor) {
        this.profileLinkColor = profileLinkColor;
    }

    public void setProfileSidebarBorderColor(String profileSidebarBorderColor) {
        this.profileSidebarBorderColor = profileSidebarBorderColor;
    }

    public void setProfileSidebarFillColor(String profileSidebarFillColor) {
        this.profileSidebarFillColor = profileSidebarFillColor;
    }

    public void setProfileTextColor(String profileTextColor) {
        this.profileTextColor = profileTextColor;
    }

    public void setProfileUseBackgroundImage(Boolean profileUseBackgroundImage) {
        this.profileUseBackgroundImage = profileUseBackgroundImage;
    }

    public void setHasExtendedProfile(Boolean hasExtendedProfile) {
        this.hasExtendedProfile = hasExtendedProfile;
    }

    public void setDefaultProfile(Boolean defaultProfile) {
        this.defaultProfile = defaultProfile;
    }

    public void setDefaultProfileImage(Boolean defaultProfileImage) {
        this.defaultProfileImage = defaultProfileImage;
    }

    public void setFollowing(Boolean following) {
        this.following = following;
    }

    public void setFollowRequestSent(Boolean followRequestSent) {
        this.followRequestSent = followRequestSent;
    }

    public void setNotifications(Boolean notifications) {
        this.notifications = notifications;
    }

    public void setTranslatorType(String translatorType) {
        this.translatorType = translatorType;
    }
}

@Parcel
class Medium {
    @SerializedName("media_url")
    @Expose
    private String mediaUrl;

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }
}