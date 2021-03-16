package models;

public class Features {

    private Double numb_of_urls;
    private Double numb_of_hashtags;
    private Double numb_of_personal_pronouns;
    private Double numb_of_present_tenses;
    private Double weighted_length;
    private Double permillage;
    private Double tweet_created_at;
    private Double tweet_id_str;
    private Double positive_sentiment;
    private Double negative_sentiment;
    private Double numb_of_mentions;
    private Double numb_of_media;
    private Double numb_of_past_tenses;
    private Double numb_of_weird_chars;
    private Double numb_of_questions;
    private Double numb_of_emoticons;
    private Double numb_of_swearing_word;
    private Double numb_of_slang_words;
    private Double numb_of_intensifiers;
    private Double tweet_length;
    private Double userFollowersCount;
    private Double userFriendsCount;
    private Double user_numb_of_tweets;
    private Double user_list_count;
    private Double dict_precision;
    private Double dict_recall;
    private Double dict_f_measure;
    private Double offset;
    private Double is_verified;

    public Features() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Features(Double numb_of_urls, Double numb_of_hashtags, Double numb_of_personal_pronouns,
                    Double numb_of_present_tenses, Double weighted_length, Double permillage, Double tweet_created_at,
                    Double tweet_id_str, Double positive_sentiment, Double negative_sentiment, Double numb_of_mentions,
                    Double numb_of_media, Double numb_of_past_tenses, Double numb_of_weird_chars, Double numb_of_questions,
                    Double numb_of_emoticons, Double numb_of_swearing_word, Double numb_of_slang_words,
                    Double numb_of_intensifiers, Double tweet_length, Double userFollowersCount, Double userFriendsCount,
                    Double user_numb_of_tweets, Double user_list_count, Double dict_precision, Double dict_recall,
                    Double dict_f_measure, Double offset, Double is_verified) {
        super();
        this.numb_of_urls = numb_of_urls;
        this.numb_of_hashtags = numb_of_hashtags;
        this.numb_of_personal_pronouns = numb_of_personal_pronouns;
        this.numb_of_present_tenses = numb_of_present_tenses;
        this.weighted_length = weighted_length;
        this.permillage = permillage;
        this.tweet_created_at = tweet_created_at;
        this.tweet_id_str = tweet_id_str;
        this.positive_sentiment = positive_sentiment;
        this.negative_sentiment = negative_sentiment;
        this.numb_of_mentions = numb_of_mentions;
        this.numb_of_media = numb_of_media;
        this.numb_of_past_tenses = numb_of_past_tenses;
        this.numb_of_weird_chars = numb_of_weird_chars;
        this.numb_of_questions = numb_of_questions;
        this.numb_of_emoticons = numb_of_emoticons;
        this.numb_of_swearing_word = numb_of_swearing_word;
        this.numb_of_slang_words = numb_of_slang_words;
        this.numb_of_intensifiers = numb_of_intensifiers;
        this.tweet_length = tweet_length;
        this.userFollowersCount = userFollowersCount;
        this.userFriendsCount = userFriendsCount;
        this.user_numb_of_tweets = user_numb_of_tweets;
        this.user_list_count = user_list_count;
        this.dict_precision = dict_precision;
        this.dict_recall = dict_recall;
        this.dict_f_measure = dict_f_measure;
        this.offset = offset;
        this.is_verified = is_verified;
    }

    public Double getNumb_of_urls() {
        return numb_of_urls;
    }

    public void setNumb_of_urls(Double numb_of_urls) {
        this.numb_of_urls = numb_of_urls;
    }

    public Double getNumb_of_hashtags() {
        return numb_of_hashtags;
    }

    public void setNumb_of_hashtags(Double numb_of_hashtags) {
        this.numb_of_hashtags = numb_of_hashtags;
    }

    public Double getNumb_of_personal_pronouns() {
        return numb_of_personal_pronouns;
    }

    public void setNumb_of_personal_pronouns(Double numb_of_personal_pronouns) {
        this.numb_of_personal_pronouns = numb_of_personal_pronouns;
    }

    public Double getNumb_of_present_tenses() {
        return numb_of_present_tenses;
    }

    public void setNumb_of_present_tenses(Double numb_of_present_tenses) {
        this.numb_of_present_tenses = numb_of_present_tenses;
    }

    public Double getWeighted_length(Double weighted_length) {
        return weighted_length;
    }

    public Double getPermillage(Double permillage) {
        return permillage;
    }

    public Double getTweet_created_at() {
        return tweet_created_at;
    }

    public void setTweet_created_at(Double tweet_created_at) {
        this.tweet_created_at = tweet_created_at;
    }

    public Double getTweet_id_str() {
        return tweet_id_str;
    }

    public void setTweet_id_str(Double tweet_id_str) {
        this.tweet_id_str = tweet_id_str;
    }

    public Double getPositive_sentiment() {
        return positive_sentiment;
    }

    public void setPositive_sentiment(Double positive_sentiment) {
        this.positive_sentiment = positive_sentiment;
    }

    public Double getNegative_sentiment() {
        return negative_sentiment;
    }

    public void setNegative_sentiment(Double negative_sentiment) {
        this.negative_sentiment = negative_sentiment;
    }

    public Double getNumb_of_mentions() {
        return numb_of_mentions;
    }

    public void setNumb_of_mentions(Double numb_of_mentions) {
        this.numb_of_mentions = numb_of_mentions;
    }

    public Double getNumb_of_media() {
        return numb_of_media;
    }

    public void setNumb_of_media(Double numb_of_media) {
        this.numb_of_media = numb_of_media;
    }

    public Double getNumb_of_past_tenses() {
        return numb_of_past_tenses;
    }

    public void setNumb_of_past_tenses(Double numb_of_past_tenses) {
        this.numb_of_past_tenses = numb_of_past_tenses;
    }

    public Double getNumb_of_weird_chars() {
        return numb_of_weird_chars;
    }

    public void setNumb_of_weird_chars(Double numb_of_weird_chars) {
        this.numb_of_weird_chars = numb_of_weird_chars;
    }

    public Double getNumb_of_questions() {
        return numb_of_questions;
    }

    public void setNumb_of_questions(Double numb_of_questions) {
        this.numb_of_questions = numb_of_questions;
    }

    public Double getNumb_of_emoticons() {
        return numb_of_emoticons;
    }

    public void setNumb_of_emoticons(Double numb_of_emoticons) {
        this.numb_of_emoticons = numb_of_emoticons;
    }

    public Double getNumb_of_swearing_word() {
        return numb_of_swearing_word;
    }

    public void setNumb_of_swearing_word(Double numb_of_swearing_word) {
        this.numb_of_swearing_word = numb_of_swearing_word;
    }

    public Double getNumb_of_slang_words() {
        return numb_of_slang_words;
    }

    public void setNumb_of_slang_words(Double numb_of_slang_words) {
        this.numb_of_slang_words = numb_of_slang_words;
    }

    public Double getNumb_of_intensifiers() {
        return numb_of_intensifiers;
    }

    public void setNumb_of_intensifiers(Double numb_of_intensifiers) {
        this.numb_of_intensifiers = numb_of_intensifiers;
    }

    public Double getTweet_length() {
        return tweet_length;
    }

    public void setTweet_length(Double tweet_length) {
        this.tweet_length = tweet_length;
    }

    public Double getUserFollowersCount() {
        return userFollowersCount;
    }

    public void setUserFollowersCount(Double userFollowersCount) {
        this.userFollowersCount = userFollowersCount;
    }

    public Double getUserFriendsCount() {
        return userFriendsCount;
    }

    public void setUserFriendsCount(Double userFriendsCount) {
        this.userFriendsCount = userFriendsCount;
    }

    public Double getUser_numb_of_tweets() {
        return user_numb_of_tweets;
    }

    public void setUser_numb_of_tweets(Double user_numb_of_tweets) {
        this.user_numb_of_tweets = user_numb_of_tweets;
    }

    public Double getUser_list_count() {
        return user_list_count;
    }

    public void setUser_list_count(Double user_list_count) {
        this.user_list_count = user_list_count;
    }

    public Double getDict_precision() {
        return dict_precision;
    }

    public void setDict_precision(Double dict_precision) {
        this.dict_precision = dict_precision;
    }

    public Double getDict_recall() {
        return dict_recall;
    }

    public void setDict_recall(Double dict_recall) {
        this.dict_recall = dict_recall;
    }

    public Double getDict_f_measure() {
        return dict_f_measure;
    }

    public void setDict_f_measure(Double dict_f_measure) {
        this.dict_f_measure = dict_f_measure;
    }

    public Double getOffset() {
        return offset;
    }

    public void setOffset(Double offset) {
        this.offset = offset;
    }

    public Double getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(Double is_verified) {
        this.is_verified = is_verified;
    }

    public Double getWeighted_length() {
        return weighted_length;
    }

    public void setWeighted_length(Double weighted_length) {
        this.weighted_length = weighted_length;
    }

    public Double getPermillage() {
        return permillage;
    }

    public void setPermillage(Double permillage) {
        this.permillage = permillage;
    }
}
