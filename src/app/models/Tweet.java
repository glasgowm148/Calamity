
package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import scala.Tuple2;

import javax.persistence.*;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by fforbeck on 24/01/15.
 *
 */
@Entity
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    @Column(name = "user_id")
    public Long userId;
    @Column(name = "user_name")
    public String userName;
    public String text;
    @Column(name = "hash_tag")
    public String hashTag;
    public String lang;
    public String sentiment;
    @Column(name = "sentiment_score")
    public Double sentimentScore;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    public Date createdAt;
    public Long retweets;

    final long serialVersionUID = 42l;
    final ObjectMapper mapper = new ObjectMapper();

    public Tweet(JsonNode json) {
        System.out.println(json);


    }

    public Tuple2<Long, String> call(String json) {
        try {
            JsonNode root = mapper.readValue(json, JsonNode.class);
            long id;
            String text;
            if (root.get("lang") != null && "en".equals(root.get("lang").textValue())) {
                if (root.get("id") != null && root.get("text") != null) {
                    id = root.get("id").longValue();
                    text = root.get("text").textValue();
                    return new Tuple2<Long, String>(id, text);
                }
                return null;
            }
            return null;
        } catch (IOException ex) {
            Logger LOG = Logger.getLogger(String.valueOf(this.getClass()));

        }
        return null;
    }
    /**
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("user_id", userId)
                .add("user_name", userName)
                .add("text", text)
                .add("hash_tag", hashTag)
                .add("lang", lang)
                .add("sentiment", sentiment)
                .add("sentiment_score", sentimentScore)
                .add("created_at", createdAt)
                .add("retweets", retweets)
                .toString();
    }
    */
}