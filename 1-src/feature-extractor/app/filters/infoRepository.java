package filters;


import java.util.ArrayList;
import java.util.List;

/**
 * infoRepository class to map the data obtained from Twitter to html pages.
 * @version 1.0
 * @see infoRepository
 * @author Navdeep Kaur Brar
 */
public class infoRepository {
	
	/**
	 * Declarations of variables in which the data obtained from twitter API is stored and then displayed in the Application
	 * @author Navdeep Kaur Brar
	 */
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
	
	/**
	 * Set the value of countList.
	 * @param countList ArrayList of type String.
	 * @author Komaldeep Singh
	 */
	public void setCountList(ArrayList<String> countList) {
		this.countList = countList;
	}
	
	/**
	 * Set the value of hashtag.
	 * @param hashtag A variable of type String.
	 * @author Navdeep Kaur Brar
	 */
	public void setHashtag(String hashtag) {
		this.hashtag = hashtag;
	}
	
	/**
	 * Set the value of searchTerms.
	 * @param searchTerms A variable of type String.
	 * @author Komaldeep Singh
	 */
	public void setSearchTerms(String searchTerms) {
		this.searchTerms = searchTerms;
	}
	
	/**
	 * Set the value of id.
	 * @param id A variable of type long.
	 * @author Navdeep Kaur Brar
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Set the value of name.
	 * @param name A variable of type String.
	 * @author Navdeep Kaur Brar
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Set the value of screenName.
	 * @param screenName A variable of type String.
	 * @author Navdeep Kaur Brar
	 */
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	
	/**
	 * Set the value of sentiment.
	 * @param sentiment A variable of type String.
	 * @author Karan Behl
	 */
	public void setSentiment(String sentiment) {
		this.sentiment = sentiment;
	}

	/**
	 * Set the value of location.
	 * @param location A variable of type String.
	 * @author Ishdeep
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Set the value of description.
	 * @param description A variable of type String.
	 * @author Navdeep Kaur Brar
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Set the value of followersCount.
	 * @param followersCount A variable of type int.
	 * @author Navdeep Kaur Brar
	 */
	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}

	/**
	 * Set the value of publicUrl.
	 * @param publicUrl A variable of type String.
	 * @author Navdeep Kaur Brar
	 */
	public void setPublicUrl(String publicUrl) {
		this.publicUrl = publicUrl;
	}

	/**
	 * Set the value of imageUrl.
	 * @param imageUrl A variable of type String.
	 * @author Navdeep Kaur Brar
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/**
	 * Set the value of tweetData.
	 * @param timeline A variable of type List.
	 * @author Navdeep Kaur Brar
	 */
	public void setTweetData(List<String> tweetData) {
		this.tweetData = tweetData;
	}
	
	/**
	 * Set the value of friendscount.
	 * @param timeline A variable of type int.
	 * @author Navdeep Kaur Brar
	 */
	public void setFriendsCount(int friendscount)
	{
		this.friendscount = friendscount;
	}

	/**
	 * Retrieve the value of id.
	 * @return A long data type.
	 * @author Navdeep Kaur Brar
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * Retrieve the value of friendsCount.
	 * @return A String data type.
	 * @author Navdeep Kaur Brar
	 */
	public int getFriendsCount()
	{
		return friendscount;
	}
	
	/**
	 * Retrieve the value of name.
	 * @return A String data type.
	 * @author Navdeep Kaur Brar
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Retrieve the value of sentiment.
	 * @return A String data type.
	 * @author Karan Behl
	 */
	public String getSentiment() {
		return sentiment;
	}

	/**
	 * Retrieve the value of screenName.
	 * @return A String data type.
	 * @author Navdeep Kaur Brar
	 */
	public String getScreenName() {
		return screenName;
	}

	/**
	 * Retrieve the value of location.
	 * @return A String data type.
	 * @author Ishdeep
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Retrieve the value of description.
	 * @return A String data type.
	 * @author Navdeep Kaur Brar
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Retrieve the value of followersCount.
	 * @return A int data type.
	 * @author Navdeep Kaur Brar
	 */
	public int getFollowersCount() {
		return followersCount;
	}

	/**
	 * Retrieve the value of publicUrl.
	 * @return A String type.
	 * @author Navdeep Kaur Brar
	 */
	public String getPublicUrl() {
		return publicUrl;
	}

	/**
	 * Retrieve the URL of imageUrl.
	 * @return A String type.
	 * @author Navdeep Kaur Brar
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * Retrieve the value of tweetData
	 * @return A String type.
	 * @author Navdeep Kaur Brar
	 */
	public List<String> getTweetData() {
		return tweetData;
	}
	
	/**
	 * Retrieve the value of hashtag
	 * @return A String type.
	 * @author Navdeep Kaur Brar
	 */
	public String getHashtag() {
		return hashtag;
	}
	
	/**
	 * Retrieve the value of countList
	 * @return An Arraylist of type String.
	 * @author Komaldeep Singh
	 */
	public ArrayList<String> getCountList() {
		return countList;
	}
	
	/**
	 * Retrieve the value of searchTerms
	 * @return A String type.
	 * @author Komaldeep Singh
	 */
	public String getSearchTerms() {
		return searchTerms;
	}
	
}
