package models;

import Utils.IDGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.annotations.Expose;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Tweet<S, O, features> {

    public Map<String, Double> setFeatures;
    private double sentimentScore;
    private String sentimentType;
    private double sentimentClassVerPos;
    private double sentimentClassPos;
    private double sentimentClassNeutral;
    private double sentimentClassNeg;
    private double sentimentClassVerNeg;
    private String[] userMentions;

    private Timestamp userRegistrationDate;

    private String userScreenName, userRealName,
            userDescription, userLocation;

    private String[] tokens;
    private int userFriendsCount;
    private int userNumbTweets;
    private int userListedCount;

    private boolean sentFromWeb, sentFromMobile;


    private String[] urls;

    private String[] hashtags;
    private Vector<Double> features;
    private double positive;
    private double negative;
    private Map<String, Double> stringDoubleMap;

    public Tweet() {
        super();

        this.id = IDGenerator.getID();
        tokens = new String[0];
        hashtags = new String[0];
        urls = new String[0];
        userMentions = new String[0];
        double[] geoLocation = new double[0];


    }

    final ObjectMapper mapper = new ObjectMapper();
    String jsonText = null;


    public int getUserListedCount() {
        return user.getListedCount();
    }

    public void setUserListedCount(int userListedCount) {
        user.setListedCount(userListedCount);
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEE MMM dd HH:mm:ss Z yyyy", locale = "en")
    @JsonProperty("created_at")
    @Expose
    private Timestamp createdAt;
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

    @JsonProperty("retweeted")
    @Expose
    private Boolean retweeted;
    @JsonProperty("data")
    private String data;
    @JsonProperty("followers_count")
    private int userFollowersCount;
    @JsonProperty("retweeted_status")
    @Expose
    private Retweet retweet;
    private final Map<String, String> properties = null;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public Tweet(JsonNode json) throws JsonProcessingException {

        // json to string
        /*
        jsonText = json.toString();
        System.out.println("jsonText" + jsonText);

        // json tree to JsonNode
        System.out.println("#### BREAKPOINT - Tweet.java - JsonNode ####");
        JsonNode tweetJsonNode = mapper.readTree(String.valueOf(json));
        System.out.println("tweetJsonNode" + tweetJsonNode);

        // Instantiates a new ObjectMapper()
        //ObjectMapper mapper = new ObjectMapper();
        //Tweet tweet = mapper.convertValue(tweetJsonNode, Tweet.class);

        //this.text = String.valueOf(json.get("full_text"));

        //System.out.println(tweet);
        //assertThat(jsonNode1.textValue(), equalTo("v1"));
*/

    }



    /* Getters and Setters */

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public Timestamp getUserRegistrationDate() {
        return userRegistrationDate;
    }

    public void setUserRegistrationDate(Timestamp userRegistrationDate) {
        this.userRegistrationDate = userRegistrationDate;
    }

    public int getUserNumbTweets() {
        return userNumbTweets;
    }

    public void setUserNumbTweets(int userNumbTweets) {
        this.userNumbTweets = userNumbTweets;
    }


    public boolean isSentFromWeb() {
        return sentFromWeb;
    }

    public void setSentFromWeb(boolean sentFromWeb) {
        this.sentFromWeb = sentFromWeb;
    }

    public boolean isSentFromMobile() {
        return sentFromMobile;
    }

    public void setSentFromMobile(boolean sentFromMobile) {
        this.sentFromMobile = sentFromMobile;
    }


    public String[] getHashtags() {
        return hashtags;
    }

    public void setHashtags(String[] hashtags) {
        this.hashtags = hashtags;
    }


    public int getUserFriendsCount() {
        return user.getFriendsCount();
    }

    public void setUserFriendsCount(int userFriendsCount) {
        user.setFriendsCount(userFriendsCount);
    }

    public int getUserFollowersCount() {
        return user.getFollowersCount();
    }

    public void setUserFollowersCount(int userFollowersCount) {
        user.setFollowersCount(userFollowersCount);
    }

    public String[] getUserMentions() {
        return userMentions;
    }

    public void setUserMentions(String[] userMentions) {
        this.userMentions = userMentions;
    }

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
    }


    public String[] getTokens() {
        return tokens;
    }

    public void setTokens(String[] tokens) {
        this.tokens = tokens;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
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

    @JsonProperty("entities")
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


    public void setSentiment(SentimentResult sentimentResult) {
        this.sentimentScore = sentimentResult.getSentimentScore();
        this.sentimentType = sentimentResult.getSentimentType();
        this.sentimentClassVerPos = sentimentResult.getSentimentClass().getVeryPositive();
        this.sentimentClassPos = sentimentResult.getSentimentClass().getPositive();
        this.sentimentClassNeutral = sentimentResult.getSentimentClass().getNeutral();
        this.sentimentClassNeg = sentimentResult.getSentimentClass().getNegative();
        this.sentimentClassVerNeg = sentimentResult.getSentimentClass().getVeryNegative();
    }


    public double getSentimentScore() {
        return this.sentimentScore;
    }

    public void setFeatureVector(Vector<Double> features) {
        this.features = features;
    }


    public Vector<Double> getFeatureVector() {
        return features;
    }

    public void setPositiveSentiment(double positive) {
        this.positive = positive;
    }

    public Double getPositiveSentiment() {
        return positive;
    }

    public void setNegativeSentiment(double negative) {
        this.negative = negative;
    }


    public Double getNegativeSentiment() {
        return negative;
    }

    public Map<String, Double> getFeatures() {
        return stringDoubleMap;
    }

    public void setFeatures(Map<String, Double> stringDoubleMap) {
        this.stringDoubleMap = stringDoubleMap;
    }

}
@JsonIgnoreProperties(ignoreUnknown = true)
class Entities {


    @JsonProperty("created_at")
    @Expose
    private String createdAt;
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

@JsonIgnoreProperties(ignoreUnknown = true)
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