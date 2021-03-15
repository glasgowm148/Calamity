package models;

import java.util.ArrayList;
import java.util.List;

public class LineProcessingResult {

	List<Tweet> tweets = new ArrayList<>();

	public LineProcessingResult(List<Tweet> tweets) {
		super();
		this.tweets = tweets;
	}

	public LineProcessingResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<Tweet> getTweets() {
		return tweets;
	}

	public void setTweets(List<Tweet> tweets) {
		this.tweets = tweets;
	}
	
	
}
