package models;

/* TweetAPI - This file displays the json */
public class TweetApi {

    private Long tweet_id;
    private String tweet_text;
    private String Embeddings;
    private String Sentiment;
    private Features features;

    public TweetApi(Long tweet_id, String tweet_text, String embeddings, String sentiment, Features features) {
        super();
        this.tweet_id = tweet_id;
        this.tweet_text = tweet_text;
        Embeddings = embeddings;
        Sentiment = sentiment;
        this.features = features;
    }

    public TweetApi() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Long getTweet_id() {
        return tweet_id;
    }

    public void setTweet_id(Long tweet_id) {
        this.tweet_id = tweet_id;
    }

    public String getTweet_text() {
        return tweet_text;
    }

    public void setTweet_text(String tweet_text) {
        this.tweet_text = tweet_text;
    }

    public String getEmbeddings() {
        return Embeddings;
    }

    public void setEmbeddings(String embeddings) {
        Embeddings = embeddings;
    }

    public String getSentiment() {
        return Sentiment;
    }

    public void setSentiment(String sentiment) {
        Sentiment = sentiment;
    }

    public Features getFeatures() {
        return features;
    }

    public void setFeatures(Features features) {
        this.features = features;
    }


}
