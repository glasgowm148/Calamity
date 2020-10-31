package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tweet {

    public Tweet() {
        super();
    }

    final ObjectMapper mapper = new ObjectMapper();
    String jsonText = null;
    @JsonProperty("created_at")
    @Expose
    private String createdAt;
    @JsonProperty("id")
    @Expose
    private Long id;
    @JsonProperty("id_str")
    @Expose
    private String idStr;
    @JsonProperty("full_text")
    @Expose
    private String text;
    @JsonProperty("truncated")
    @Expose
    private Boolean truncated;
    @JsonProperty("entities")
    @Expose
    private Entities entities;
    @JsonProperty("user")
    @Expose
    private User user;
    @JsonProperty("is_quote_status")
    @Expose
    private Boolean isQuoteStatus;
    @JsonProperty("retweet_count")
    @Expose
    private Integer retweetCount;
    @JsonProperty("favorite_count")
    @Expose
    private Integer favoriteCount;
    @JsonProperty("favorited")
    @Expose
    private Boolean favorited;
    @JsonProperty("retweeted")
    @Expose
    private Boolean retweeted;
    @JsonProperty("retweeted_status")
    @Expose
    private Retweet retweet;
    private final Map<String, String> properties = null;


    public Tweet(JsonNode json) throws JsonProcessingException {

        jsonText = json.toString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode tweetJsonNode = mapper.readTree(String.valueOf(json));
        mapper.readTree(String.valueOf(json));
        this.text = String.valueOf(json.get("full_text"));

        // JsonNode jsonNode1 = json.get("full_text");

        System.out.println("####BREAKPOINT - Tweet.java - this.getText()####");
        System.out.println(this.getText());
        System.out.println(tweetJsonNode.toString());
       //System.out.println(mapper.convertValue(json, Tweet.class)); // Unrecognized field "full_text" (class models.Tweet),

        //Tweet tweet = mapper.convertValue(json, Tweet.class);
        //System.out.println(tweet);
        //assertThat(jsonNode1.textValue(), equalTo("v1"));

        // doParsing(tweet);

        /*
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
*/
    }



    /* Getters and Setters */

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getTruncated() {
        return truncated;
    }

    public void setTruncated(Boolean truncated) {
        this.truncated = truncated;
    }

    public Entities getEntities() {
        return entities;
    }

    public void setEntities(Entities entities) {
        this.entities = entities;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getQuoteStatus() {
        return isQuoteStatus;
    }

    public void setQuoteStatus(Boolean quoteStatus) {
        isQuoteStatus = quoteStatus;
    }

    public Integer getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(Integer retweetCount) {
        this.retweetCount = retweetCount;
    }

    public Integer getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(Integer favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public Boolean getFavorited() {
        return favorited;
    }

    public void setFavorited(Boolean favorited) {
        this.favorited = favorited;
    }

    public Boolean getRetweeted() {
        return retweeted;
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

}

class Entities {
    @JsonIgnoreProperties(ignoreUnknown = true)



    @JsonProperty("hashtags")
    @Expose
    private String[] hashtags;

    @JsonProperty("symbols")
    @Expose
    private String[] symbols;

    @JsonProperty("user_mentions")
    @Expose
    private String[] user_mentions;

    @JsonProperty("urls")
    @Expose
    private List<Object>  urls;

    @JsonProperty("media")
    @Expose
    private List<Medium> media = new ArrayList<Medium>();

    Entities() {
    }

    public List<Medium> getMedia() {
        return media;
    }

    public void setMedia(List<Medium> media) {
        this.media = media;
    }
}


class Retweet {

    @JsonProperty("user")
    @Expose
    private User user;

    @JsonProperty("favorite_count")
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

@JsonIgnoreProperties(ignoreUnknown = true)
class User {


    @JsonProperty("id")
    @Expose
    private Long id;
    @JsonProperty("id_str")
    @Expose
    private String idStr;
    @JsonProperty("name")
    @Expose
    private String name;
    @JsonProperty("screen_name")
    @Expose
    private String screenName;
    @JsonProperty("location")
    @Expose
    private String location;
    @JsonProperty("description")
    @Expose
    private String description;
    @JsonProperty("url")
    @Expose
    private String url;
    @JsonProperty("followers_count")
    @Expose
    private Integer followersCount;
    @JsonProperty("friends_count")
    @Expose
    private Integer friendsCount;
    @JsonProperty("listed_count")
    @Expose
    private Integer listedCount;
    @JsonProperty("created_at")
    @Expose
    private String createdAt;
    @JsonProperty("favourites_count")
    @Expose
    private Integer favouritesCount;
    @JsonProperty("utc_offset")
    @Expose
    private Integer utcOffset;
    @JsonProperty("time_zone")
    @Expose
    private String timeZone;
    @JsonProperty("geo_enabled")
    @Expose
    private Boolean geoEnabled;
    @JsonProperty("verified")
    @Expose
    private Boolean verified;
    @JsonProperty("statuses_count")
    @Expose
    private Integer statusesCount;
    @JsonProperty("lang")
    @Expose
    private String profileBackgroundColor;
    @JsonProperty("profile_background_image_url")
    @Expose
    private String profileBackgroundImageUrl;
    @JsonProperty("profile_background_image_url_https")
    @Expose
    private String profileBackgroundImageUrlHttps;
    @JsonProperty("profile_background_tile")
    @Expose
    private Boolean profileBackgroundTile;
    @JsonProperty("profile_image_url")
    @Expose
    private String profileImageUrl;
    @JsonProperty("profile_image_url_https")
    @Expose
    private String profileImageUrlHttps;
    @JsonProperty("profile_banner_url")
    @Expose
    private String profileBannerUrl;
    @JsonProperty("profile_link_color")
    @Expose
    private String profileLinkColor;
    @JsonProperty("profile_sidebar_border_color")
    @Expose
    private String profileSidebarBorderColor;
    @JsonProperty("profile_sidebar_fill_color")
    @Expose
    private String profileSidebarFillColor;
    @JsonProperty("profile_text_color")
    @Expose
    private String profileTextColor;
    @JsonProperty("profile_use_background_image")
    @Expose
    private Boolean profileUseBackgroundImage;
    @JsonProperty("has_extended_profile")
    @Expose
    private Boolean hasExtendedProfile;
    @JsonProperty("default_profile")
    @Expose
    private Boolean defaultProfile;
    @JsonProperty("default_profile_image")
    @Expose
    private Boolean defaultProfileImage;
    @JsonProperty("following")
    @Expose
    private Boolean following;
    @JsonProperty("follow_request_sent")
    @Expose
    private Boolean followRequestSent;
    @JsonProperty("notifications")
    @Expose
    private Boolean notifications;
    @JsonProperty("translator_type")
    @Expose
    private String translatorType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    public Integer getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(Integer friendsCount) {
        this.friendsCount = friendsCount;
    }

    public Integer getListedCount() {
        return listedCount;
    }

    public void setListedCount(Integer listedCount) {
        this.listedCount = listedCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getFavouritesCount() {
        return favouritesCount;
    }

    public void setFavouritesCount(Integer favouritesCount) {
        this.favouritesCount = favouritesCount;
    }

    public Integer getUtcOffset() {
        return utcOffset;
    }

    public void setUtcOffset(Integer utcOffset) {
        this.utcOffset = utcOffset;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Boolean getGeoEnabled() {
        return geoEnabled;
    }

    public void setGeoEnabled(Boolean geoEnabled) {
        this.geoEnabled = geoEnabled;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Integer getStatusesCount() {
        return statusesCount;
    }

    public void setStatusesCount(Integer statusesCount) {
        this.statusesCount = statusesCount;
    }

    public String getProfileBackgroundColor() {
        return profileBackgroundColor;
    }

    public void setProfileBackgroundColor(String profileBackgroundColor) {
        this.profileBackgroundColor = profileBackgroundColor;
    }

    public String getProfileBackgroundImageUrl() {
        return profileBackgroundImageUrl;
    }

    public void setProfileBackgroundImageUrl(String profileBackgroundImageUrl) {
        this.profileBackgroundImageUrl = profileBackgroundImageUrl;
    }

    public String getProfileBackgroundImageUrlHttps() {
        return profileBackgroundImageUrlHttps;
    }

    public void setProfileBackgroundImageUrlHttps(String profileBackgroundImageUrlHttps) {
        this.profileBackgroundImageUrlHttps = profileBackgroundImageUrlHttps;
    }

    public Boolean getProfileBackgroundTile() {
        return profileBackgroundTile;
    }

    public void setProfileBackgroundTile(Boolean profileBackgroundTile) {
        this.profileBackgroundTile = profileBackgroundTile;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getProfileImageUrlHttps() {
        return profileImageUrlHttps;
    }

    public void setProfileImageUrlHttps(String profileImageUrlHttps) {
        this.profileImageUrlHttps = profileImageUrlHttps;
    }

    public String getProfileBannerUrl() {
        return profileBannerUrl;
    }

    public void setProfileBannerUrl(String profileBannerUrl) {
        this.profileBannerUrl = profileBannerUrl;
    }

    public String getProfileLinkColor() {
        return profileLinkColor;
    }

    public void setProfileLinkColor(String profileLinkColor) {
        this.profileLinkColor = profileLinkColor;
    }

    public String getProfileSidebarBorderColor() {
        return profileSidebarBorderColor;
    }

    public void setProfileSidebarBorderColor(String profileSidebarBorderColor) {
        this.profileSidebarBorderColor = profileSidebarBorderColor;
    }

    public String getProfileSidebarFillColor() {
        return profileSidebarFillColor;
    }

    public void setProfileSidebarFillColor(String profileSidebarFillColor) {
        this.profileSidebarFillColor = profileSidebarFillColor;
    }

    public String getProfileTextColor() {
        return profileTextColor;
    }

    public void setProfileTextColor(String profileTextColor) {
        this.profileTextColor = profileTextColor;
    }

    public Boolean getProfileUseBackgroundImage() {
        return profileUseBackgroundImage;
    }

    public void setProfileUseBackgroundImage(Boolean profileUseBackgroundImage) {
        this.profileUseBackgroundImage = profileUseBackgroundImage;
    }

    public Boolean getHasExtendedProfile() {
        return hasExtendedProfile;
    }

    public void setHasExtendedProfile(Boolean hasExtendedProfile) {
        this.hasExtendedProfile = hasExtendedProfile;
    }

    public Boolean getDefaultProfile() {
        return defaultProfile;
    }

    public void setDefaultProfile(Boolean defaultProfile) {
        this.defaultProfile = defaultProfile;
    }

    public Boolean getDefaultProfileImage() {
        return defaultProfileImage;
    }

    public void setDefaultProfileImage(Boolean defaultProfileImage) {
        this.defaultProfileImage = defaultProfileImage;
    }

    public Boolean getFollowing() {
        return following;
    }

    public void setFollowing(Boolean following) {
        this.following = following;
    }

    public Boolean getFollowRequestSent() {
        return followRequestSent;
    }

    public void setFollowRequestSent(Boolean followRequestSent) {
        this.followRequestSent = followRequestSent;
    }

    public Boolean getNotifications() {
        return notifications;
    }

    public void setNotifications(Boolean notifications) {
        this.notifications = notifications;
    }

    public String getTranslatorType() {
        return translatorType;
    }

    public void setTranslatorType(String translatorType) {
        this.translatorType = translatorType;
    }
}


class Medium {
    @JsonProperty("media_url")
    @Expose
    private String mediaUrl;

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }
}