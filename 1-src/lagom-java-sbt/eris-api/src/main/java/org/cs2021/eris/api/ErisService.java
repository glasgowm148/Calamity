package org.cs2021.eris.api;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.api.broker.kafka.KafkaProperties;

import static com.lightbend.lagom.javadsl.api.Service.*;

/**
 * The eris service interface.
 * <p>
 * This describes everything that Lagom needs to know about how to serve and
 * consume the ErisService.
 */
public interface ErisService extends Service {
    /**
     * Example:
     * curl http://localhost:9000/api/hello/Alice
     */
    ServiceCall<NotUsed, String> hello(String id);

    /**
     * Example:
     * curl -H "Content-Type: application/json" -X POST -d '{"message": "Hi"}' http://localhost:9000/api/hello/Alice
     */
    ServiceCall<GreetingMessage, Done> useGreeting(String id);

    /**
     * This gets published to Kafka.
     */
    Topic<ErisEvent> helloEvents();

    @Override
    default Descriptor descriptor() {
        return named("eris")
                .withCalls(
                        pathCall("/api/hello/:id", this::hello),
                        pathCall("/api/hello/:id", this::useGreeting)
                )
                .withTopics(
                        topic("hello-events", this::helloEvents)
                                // Kafka partitions messages, messages within the same partition will
                                // be delivered in order, to ensure that all messages for the same user
                                // go to the same partition (and hence are delivered in order with respect
                                // to that user), we configure a partition key strategy that extracts the
                                // name as the partition key.
                                .withProperty(KafkaProperties.partitionKeyStrategy(), ErisEvent::getName)
                )
                .withAutoAcl(true);
    }
}
