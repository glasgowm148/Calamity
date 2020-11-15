package controllers;

import API.TweetStore;
import Utils.APIUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import models.Tweet;
import play.core.j.HttpExecutionContext;
import play.libs.Json;
import play.mvc.Result;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static play.mvc.Results.notFound;
import static play.mvc.Results.ok;


public class TweetController {

    //private HttpExecutionContext ec;
    private final TweetStore tweetStore;

    @Inject
    public TweetController(TweetStore tweetStore) {
        this.tweetStore = tweetStore;
        //this.ec = ec;
    }

    public CompletionStage<Result> retrieve(int id) {
        return supplyAsync(() -> {
            final Optional<Tweet> tweetOptional = tweetStore.getTweet(id);
            return tweetOptional.map(student -> {
                JsonNode jsonObjects = Json.toJson(tweetStore);
                return ok(APIUtils.createResponse(jsonObjects, true));
            }).orElse(notFound(APIUtils.createResponse("Tweet with id:" + id + " not found", false)));
        }); //,ec.current());
    }
}