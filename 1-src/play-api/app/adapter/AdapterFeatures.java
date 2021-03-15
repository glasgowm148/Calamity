package adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import constant.Constants;
import models.Features;
import models.Tweet;
import models.TweetApi;

public class AdapterFeatures {

    public AdapterFeatures() {
        super();
    }

    /**
     * Tweet API adaptor
     *
     * @param output - The output
     * @throws JsonProcessingException
     * @returns the tweet OR null
     */
    public static TweetApi adapterTweet(final Tweet output) throws JsonProcessingException {
        TweetApi tweet = new TweetApi();
        if (output != null) {
            if (output.getId() != null) {
                tweet.setTweet_id(output.getId());
            }

            if (output.getText() != null) {
                tweet.setTweet_text(output.getText());
            }
            Features feature = new Features();
            System.out.println(output.getFeatures()); // stringDoubleMap from Tweet.java
            if (output.getFeatures() != null) {
                if (output.getFeatures().get(Constants.NUMB_OF_URLS) != null) {
                    feature.setNumb_of_hashtags(output.getFeatures().get(Constants.NUMB_OF_URLS));
                }
                if (output.getFeatures().get(Constants.NUMB_OF_HASHTAGS) != null) {
                    feature.setNumb_of_hashtags(output.getFeatures().get(Constants.NUMB_OF_HASHTAGS));
                }
                if (output.getFeatures().get(Constants.NUMB_OF_PERSONAL_PRONOUNS) != null) {
                    feature.setNumb_of_personal_pronouns(output.getFeatures().get(Constants.NUMB_OF_PERSONAL_PRONOUNS));
                }
                if (output.getFeatures().get(Constants.NUMB_OF_PRESENT_TENSES) != null) {
                    feature.setNumb_of_present_tenses(output.getFeatures().get(Constants.NUMB_OF_PRESENT_TENSES));
                }
                if (output.getFeatures().get(Constants.WEIGHTED_LENGTH) != null) {
                    feature.setWeighted_length(output.getFeatures().get(Constants.WEIGHTED_LENGTH));
                }
                if (output.getFeatures().get(Constants.PERMILLAGE) != null) {
                    feature.setPermillage(output.getFeatures().get(Constants.PERMILLAGE));
                }

                if (output.getFeatures().get(Constants.TWEET_CREATED_AT) != null) {
                    feature.setTweet_created_at(output.getFeatures().get(Constants.TWEET_CREATED_AT));

                }
                if (output.getFeatures().get(Constants.TWEET_ID_STR) != null) {
                    feature.setTweet_id_str(output.getFeatures().get(Constants.TWEET_ID_STR));

                }
                if (output.getFeatures().get(Constants.POSITIVE_SENTIMENT) != null) {
                    feature.setPositive_sentiment(output.getFeatures().get(Constants.POSITIVE_SENTIMENT));

                }
                if (output.getFeatures().get(Constants.NEGATIVE_SENTIMENT) != null) {
                    feature.setNegative_sentiment(output.getFeatures().get(Constants.NEGATIVE_SENTIMENT));

                }
                if (output.getFeatures().get(Constants.NUMB_OF_MENTIONS) != null) {
                    feature.setNumb_of_mentions(output.getFeatures().get(Constants.NUMB_OF_MENTIONS));

                }
                if (output.getFeatures().get(Constants.NUMB_OF_MEDIA) != null) {
                    feature.setNumb_of_media(output.getFeatures().get(Constants.NUMB_OF_MEDIA));

                }
                if (output.getFeatures().get(Constants.NUMB_OF_PAST_TENSES) != null) {
                    feature.setNumb_of_past_tenses(output.getFeatures().get(Constants.NUMB_OF_PAST_TENSES));

                }
                if (output.getFeatures().get(Constants.NUMB_OF_WEIRD_CHARS) != null) {
                    feature.setNumb_of_weird_chars(output.getFeatures().get(Constants.NUMB_OF_WEIRD_CHARS));

                }
                if (output.getFeatures().get(Constants.NUMB_OF_QUESTIONS) != null) {
                    feature.setNumb_of_questions(output.getFeatures().get(Constants.NUMB_OF_QUESTIONS));

                }
                if (output.getFeatures().get(Constants.NUMB_OF_EMOTICONS) != null) {
                    feature.setNumb_of_emoticons(output.getFeatures().get(Constants.NUMB_OF_EMOTICONS));

                }
                if (output.getFeatures().get(Constants.NUMB_OF_SWEARING_WORD) != null) {
                    feature.setNumb_of_swearing_word(output.getFeatures().get(Constants.NUMB_OF_SWEARING_WORD));

                }
                if (output.getFeatures().get(Constants.NUMB_OF_SLANG_WORDS) != null) {
                    feature.setNumb_of_slang_words(output.getFeatures().get(Constants.NUMB_OF_SLANG_WORDS));

                }
                if (output.getFeatures().get(Constants.NUMB_OF_INTENSIFIERS) != null) {
                    feature.setNumb_of_intensifiers(output.getFeatures().get(Constants.NUMB_OF_INTENSIFIERS));

                }
                if (output.getFeatures().get(Constants.TWEET_LENGTH) != null) {
                    feature.setTweet_length(output.getFeatures().get(Constants.TWEET_LENGTH));

                }
                if (output.getFeatures().get(Constants.USERFOLLOWERSCOUNT) != null) {
                    feature.setUserFollowersCount(output.getFeatures().get(Constants.USERFOLLOWERSCOUNT));

                }

                if (output.getFeatures().get(Constants.USERFRIENDSCOUNT) != null) {
                    feature.setUserFriendsCount(output.getFeatures().get(Constants.USERFRIENDSCOUNT));

                }
                if (output.getFeatures().get(Constants.USER_NUMB_OF_TWEETS) != null) {
                    feature.setUser_numb_of_tweets(output.getFeatures().get(Constants.USER_NUMB_OF_TWEETS));

                }
                if (output.getFeatures().get(Constants.USER_LIST_COUNT) != null) {
                    feature.setUser_list_count(output.getFeatures().get(Constants.USER_LIST_COUNT));

                }
                if (output.getFeatures().get(Constants.DICT_PRECISION) != null) {
                    feature.setDict_precision(output.getFeatures().get(Constants.DICT_PRECISION));

                }
                if (output.getFeatures().get(Constants.DICT_RECALL) != null) {
                    feature.setDict_recall(output.getFeatures().get(Constants.DICT_RECALL));

                }
                if (output.getFeatures().get(Constants.DICT_F_MEASURE) != null) {
                    feature.setDict_f_measure(output.getFeatures().get(Constants.DICT_F_MEASURE));

                }
                if (output.getFeatures().get(Constants.OFFSET) != null) {
                    feature.setOffset(output.getFeatures().get(Constants.OFFSET));

                }
                if (output.getFeatures().get(Constants.IS_VERIFIED) != null) {
                    feature.setIs_verified(output.getFeatures().get(Constants.IS_VERIFIED));

                }
            }

            tweet.setFeatures(feature);

            // Convert the word embeddings into a json object
            if (output.getDimensions() != null && output.getDimensions().length != 0) {
                tweet.setEmbeddings(convertTable(output.getDimensions()));
            }

            // Convert the word embeddings into a json object
            if (output.getVectorTree() != null) {
                tweet.setSentiment(output.getVectorTree().toString().replace("\n", ","));
            }

            return tweet;

        } else {
            return null;
        }
    }

    private static String convertTable(float[] dimensions) {
        StringBuilder dimensionConverter = new StringBuilder();
        dimensionConverter.append("{");

        for (float dimension : dimensions) {
            dimensionConverter.append(dimension);
            dimensionConverter.append(",");
        }
        dimensionConverter.append("}");
        return dimensionConverter.toString();
    }
}
