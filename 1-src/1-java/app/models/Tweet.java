package models;

import Utils.IDGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Tweet implements Comparable<Tweet> {

    public Map<String, Double> setFeatures;
    private double sentimentScore;
    public String[] locations;
    public List<String> simpleTokens;
    public boolean isFavorited, isPossiblySensitive,
            isRetweet, isRetweetedByMe, isTruncated;

    private Timestamp userRegistrationDate;

    private String userDescription;


    private int userNumbTweets;

    private boolean sentFromWeb, sentFromMobile;


    private List<String> hashtags;
    private Vector<Double> features;
    private double positive;
    private double negative;
    private Map<String, Double> stringDoubleMap;
    private double tfidf;
    private List<Float> result;
    private int offset;
    private int weightedLength;
    private int permillage;
    private float[] dimensions;
    private String[] tokens;

    public Tweet() {
        super();

        this.id = IDGenerator.getID();
        tokens = new String[0];

    }



    public int getUserListedCount() {
        return user.getListedCount();
    }

    public void setUserListedCount(int userListedCount) {
        user.setListedCount(userListedCount);
    }

    public int getIsVerified() {
        return user.getVerified() ? 1 : 0;
    }

    public void setIsVerified(boolean isVerified) {
        user.setVerified(isVerified);
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


    public List<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<String> hashtags) {
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

    public List<Object> getUserMentions() {
        return entities.getEntityMentions();
    }

    public void setUserMentions(List<Object> user_mentions) {
        entities.setEntityMentions(user_mentions);
    }

    public List<Medium> getMedia() {
        return entities.getMedia();
    }

    public void setMedia(List<Medium> media) {
        entities.setMedia(media);
    }
    
    public List<Object> getUrls() {
        return entities.getEntityUrls();
    }

    public String[] getTokens() {
        return tokens;
    }

    public void setTokens(String[] tokens) {
        this.tokens = tokens;
    }

    public int getCreatedAtInt() {
        return (int)(createdAt.getTime() /1000 );
    }
    public String getCreatedAtStr() {
        return String.valueOf((int)(createdAt.getTime() /1000 ));
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
        String sentimentType = sentimentResult.getSentimentType();
        double sentimentClassVerPos = sentimentResult.getSentimentClass().getVeryPositive();
        double sentimentClassPos = sentimentResult.getSentimentClass().getPositive();
        double sentimentClassNeutral = sentimentResult.getSentimentClass().getNeutral();
        double sentimentClassNeg = sentimentResult.getSentimentClass().getNegative();
        double sentimentClassVerNeg = sentimentResult.getSentimentClass().getVeryNegative();
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

    public void setTFIDF(double tfidf) {
        this.tfidf = tfidf;
    }

    public double getTFIDF(){
        return tfidf;
    }

    public void setResult(List<Float> result) {
        this.result = result;
    }

    public Float getResult(int i) {
        Float f = (float) 0;
        if(result != null){
            return result.get(i);
        }
        return f;

    }

    public void setAnalysis(Serializable analyse) {
    }

    public void setOffset(int i) {
        this.offset = i; 
    }

    public Double getOffset() {
        return (double) offset;
    }

    public void setWeightedLength(int weightedLength) {
        this.weightedLength = weightedLength;
    }

    public void setPermillage(int permillage) {
        this.permillage = permillage;
    }

    public int getWeightedLength() {
        return this.weightedLength;
    }

    public int getPermillage() {
        return this.permillage;
    }

    public void setDimensions(float[] d) {
        this.dimensions = d;
    }

    public float[] getDimensions() {
        return this.dimensions;
    }

    @Override
    public int compareTo(Tweet o) {
        if ( createdAt.getTime() > o.getCreatedAt().getTime() )
            return 1;
        else if( createdAt.getTime() < o.getCreatedAt().getTime() )
            return -1;
        else return 0;
    }

    public String[] getLocations() {
        return locations;
    }

    public void setLocations(String[] locations) {
        this.locations = locations;
    }

    public List<String> getSimpleTokens() {
        return simpleTokens;
    }

    public void setSimpleTokens(List<String> simpleTokens) {
        this.simpleTokens = simpleTokens;
    }

    public boolean isFavorited() {
        return isFavorited;
    }

    public void setFavorited(boolean isFavorited) {
        this.isFavorited = isFavorited;
    }

    public boolean isPossiblySensitive() {
        return isPossiblySensitive;
    }

    public void setPossiblySensitive(boolean isPossiblySensitive) {
        this.isPossiblySensitive = isPossiblySensitive;
    }

    public boolean isRetweet() {
        return isRetweet;
    }

    public void setRetweet(boolean isRetweet) {
        this.isRetweet = isRetweet;
    }

    public boolean isRetweetedByMe() {
        return isRetweetedByMe;
    }

    public void setRetweetedByMe(boolean isRetweetedByMe) {
        this.isRetweetedByMe = isRetweetedByMe;
    }

    public boolean isTruncated() {
        return isTruncated;
    }

    public void setTruncated(boolean isTruncated) {
        this.isTruncated = isTruncated;
    }
}
@JsonIgnoreProperties(ignoreUnknown = true)

class Entities {

    Entities() {

    }
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEE MMM dd HH:mm:ss Z yyyy", locale = "en")
    @JsonProperty("created_at")
    @Expose
    private String createdAt;

    @JsonProperty("user_mentions")
    @Expose
    private List<Object> user_mentions;

    @JsonProperty("urls")
    @Expose
    private List<Object> urls;

    @JsonProperty("media")
    @Expose
    private List<Medium> media = new ArrayList<Medium>();

    public void setEntityMentions(List<Object>  user_mentions){
        this.user_mentions = user_mentions;
    }

    public List<Object> getEntityMentions(){
        return user_mentions;
    }

    public void setEntityUrls(List<Object>  urls){
        this.urls = urls;
    }

    public List<Object> getEntityUrls(){
        return urls;
    }

    public List<Medium> getMedia() {
        return media;
    }

    public void setMedia(List<Medium> media) {
        this.media = media;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Retweet {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEE MMM dd HH:mm:ss Z yyyy", locale = "en")
    @JsonProperty("created_at")
    @Expose
    private String createdAt;

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
    // https://developer.twitter.com/en/docs/twitter-api/v1/data-dictionary/object-model/user


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
    // https://developer.twitter.com/en/docs/twitter-api/v1/data-dictionary/object-model/entities#media
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

