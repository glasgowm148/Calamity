package lib;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Simple streamed reader to go through Lined JSON files, convert each line to POJO entry
 * and perform a specified action on every row.
 * @author Vladimir Salin @ SwiftDil
 */
public class LineBasedJsonReader {

    private static final Logger log = LoggerFactory.getLogger(LineBasedJsonReader.class);
    private ObjectMapper objectMapper;

    public LineBasedJsonReader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Parses a provided input in a streamed way. Converts each line in it
     * (which is supposed to be a JSON) to a specified POJO class
     * and performs an action provided as a Java 8 Consumer.
     *
     * @param stream lined JSON input
     * @param entryClass POJO class to convert JSON to
     * @param consumer action to perform on each entry
     * @return number of rows read
     */
    public int parseAsStream(final InputStream stream, final Class entryClass, final Consumer<? super Object> consumer) {
        long start = System.currentTimeMillis();

        final AtomicInteger total = new AtomicInteger(0);
        final AtomicInteger failed = new AtomicInteger(0);

        try (Stream<String> lines = new BufferedReader(new InputStreamReader(stream)).lines()) {
            lines
                    .map(line -> {
                        try {
                            total.incrementAndGet();
                            return objectMapper.readerFor(entryClass).readValue(line);
                        } catch (IOException e) {
                            log.error("Failed to parse a line {}. Reason: {}", total.get()-1, e.getMessage());
                            log.debug("Stacktrace: ", e);
                            failed.incrementAndGet();
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .forEach(consumer);
        }
        long took = System.currentTimeMillis() - start;
        log.info("Parsed {} lines with {} failures. Took {}ms", total.get(), failed.get(), took);

        return total.get() - failed.get();
    }
}
