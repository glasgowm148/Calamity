import java.util.ArrayList;
import java.util.List;

// TODO - convert timestamp to timestamp data type
// TODO - insert tweet in DB with legitimate timestamp format
public class Tweet {
	private Integer tweetID;
	private Integer anon1;
	private Integer anon2;
	private java.sql.Timestamp timestamp;
//	private String timestamp ;
	private String label;
	private String username;
	private String tweet;
	private ArrayList<String> hashtags;
	private ArrayList<String> entities;
	private ArrayList<String> locations;
	private String assignedTopic;
	public String getAssignedTopic() {
		return assignedTopic;
	}
	public void setAssignedTopic(String assignedTopic) {
		this.assignedTopic = assignedTopic;
	}
	public ArrayList<String> getEntities() {
		return entities;
	}
	public void setEntities(ArrayList<String> entities) {
		this.entities = entities;
	}
	Tweet()
	{
		hashtags = new ArrayList<String>();
	}
	public Integer getAnon1() {
		return anon1;
	}
	public void setAnon1(Integer anon1) {
		this.anon1 = anon1;
	}
	public Integer getAnon2() {
		return anon2;
	}
	public void setAnon2(Integer anon2) {
		this.anon2 = anon2;
	}
	public java.sql.Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(java.sql.Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTweet() {
		return tweet;
	}
	public void setTweet(String tweet) {
		this.tweet = tweet.toLowerCase();
	}
	public Integer getTweetID() {
		return tweetID;
	}
	public void setTweetID(Integer tweetID) {
		this.tweetID = tweetID;
	}
	public List<String> getHashtags() {
		return hashtags;
	}
	public void setHashtags(ArrayList<String> hashtags) {
		this.hashtags = hashtags;
	}
	public void addHashtags(String h) {
		this.hashtags.add(h);
	}
	public ArrayList<String> getLocations() {
		return locations;
	}
	public void setLocations(ArrayList<String> locations) {
		this.locations = locations;
	}
	
}
