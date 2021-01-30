package Utils;

import java.util.ArrayList;
import java.util.List;


public class infoRepository {



    public long id;
    public String name;
    public String email;
    public String screenName;
    public String location;
    public String sentiment;
    public String description;
    public String imageUrl;
    public int followersCount;
    public String publicUrl;
    public List<String> tweetData;
    public int friendscount;
    public String hashtag;
    public ArrayList<String> countList;
    public String searchTerms;


    public void setCountList(ArrayList<String> countList) {
        this.countList = countList;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public void setSearchTerms(String searchTerms) {
        this.searchTerms = searchTerms;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public void setPublicUrl(String publicUrl) {
        this.publicUrl = publicUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setTweetData(List<String> tweetData) {
        this.tweetData = tweetData;
    }

    public void setFriendsCount(int friendscount)
    {
        this.friendscount = friendscount;
    }

    public long getId() {
        return id;
    }

    public int getFriendsCount()
    {
        return friendscount;
    }

    public String getName() {
        return name;
    }

    public String getSentiment() {
        return sentiment;
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

    public int getFollowersCount() {
        return followersCount;
    }

    public String getPublicUrl() {
        return publicUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<String> getTweetData() {
        return tweetData;
    }

    public String getHashtag() {
        return hashtag;
    }

    public ArrayList<String> getCountList() {
        return countList;
    }

    public String getSearchTerms() {
        return searchTerms;
    }

}
