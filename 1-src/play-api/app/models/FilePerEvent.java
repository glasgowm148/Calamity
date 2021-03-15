package models;

import java.util.ArrayList;
import java.util.List;


public class FilePerEvent {

    private String fileName;
    private List<TweetApi> tweets = new ArrayList<>();

    public FilePerEvent() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getfileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<TweetApi> getTweets() {
        return tweets;
    }

    public void setTweets(List<TweetApi> tweets) {
        this.tweets = tweets;
    }


}
