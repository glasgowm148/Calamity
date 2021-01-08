package actors;

import models.Tweet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.chen0040.embeddings.GloVeModel;

public class gloveActor {

    private static final Logger logger = LoggerFactory.getLogger(gloveActor.class);
    int dimensions = 50;

    public gloveActor(GloVeModel model, Tweet tweet) {

        /**
        logger.info("word2em size: {}", model.size());
        logger.info("word2em dimension for individual word: {}", model.getWordVecDimension());

        logger.info("father: {}", model.encodeWord("father"));
        logger.info("mother: {}", model.encodeWord("mother"));
        logger.info("man: {}", model.encodeWord("man"));
        logger.info("woman: {}", model.encodeWord("woman"));
        logger.info("boy: {}", model.encodeWord("boy"));
        logger.info("girl: {}", model.encodeWord("girl"));

        logger.info("distance between boy and girl: {}", model.distance("boy", "girl"));

        logger.info("doc: {}", model.encodeDocument(tweet.getText()));
        **/
        float[] d = model.encodeDocument(tweet.getText());
        tweet.setDimensions(d);


    }
}