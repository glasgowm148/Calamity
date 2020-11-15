package API;

import models.Tweet;

import java.util.*;

// https://en.getdocs.org/rest-api-with-play/

public class TweetStore {

    private final Map<Integer, Tweet> tweets = new HashMap<>();

    public Optional<Tweet> addTweet(Tweet tweet) {
        int id = tweets.size();
        tweet.setId((long) id);
        tweets.put(id, tweet);
        return Optional.of(tweet);
    }

    public Optional<Tweet> getTweet(int id) {

        for (Map.Entry<Integer, Tweet> tweet : tweets.entrySet())
            System.out.println("Key = " + tweet.getKey() +
                    ", Value = " + tweet.getValue());
        return Optional.ofNullable(tweets.get(id));
    }

    public Set<Tweet> getAllTweets() {
        return new HashSet<>(tweets.values());
    }

    public Optional<Tweet> updateTweet(Tweet tweet) {
        int id = Math.toIntExact(tweet.getId());
        if (tweets.containsKey(id)) {
            tweets.put(id, tweet);
            return Optional.ofNullable(tweet);
        }
        return Optional.empty();
    }





    public boolean deleteTweet(int id) {
        return tweets.remove(id) != null;
    }
}