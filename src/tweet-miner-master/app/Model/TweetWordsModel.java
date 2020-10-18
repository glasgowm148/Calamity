package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.stream.Collectors;
import static java.util.Comparator.reverseOrder;

/**
 * This controller generated word level statistics based on 100 tweets for a search query
 * 
 * @author nileesha
 *
 */
public class TweetWordsModel {

	/**
	 * @author nileesha
	 * Returns map representing word-level statistics
	 * @param tweetList array of JSON objects representing tweets
	 * @return map containing word and count in descending order of count
	 */
	public static Map<String, Long> tweetWords(ArrayNode tweetList) {

		List<String>tweetTexts = new ArrayList<String>();

		for (JsonNode on: tweetList) {
			tweetTexts.add(on.get("tweetsText").textValue());
		}

		return findWordLevelStatistic(tweetTexts);
	}

	/**
	 * @author nileesha
	 * Computes word level statistics
	 * @param tweetTexts arraylist containg tweet texts
	 * @return map representing word-level statistics
	 */
	public static Map<String, Long> findWordLevelStatistic(List<String> tweetTexts) {
		return tweetTexts.stream()
				.map(tweet -> tweet.split("\\s+"))
				.flatMap(Arrays::stream)
				.map(tweet-> tweet.toLowerCase())
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
						.entrySet().stream()
						.sorted(Map.Entry.<String, Long> comparingByValue(reverseOrder()).thenComparing(Map.Entry.comparingByKey()))
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
	}


}