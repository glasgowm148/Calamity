package org.cs2021.erisstream.impl;

import akka.Done;
import akka.stream.javadsl.Flow;

import org.cs2021.eris.api.ErisEvent;
import org.cs2021.eris.api.ErisService;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

/**
 * This subscribes to the ErisService event stream.
 */
public class ErisStreamSubscriber {
    @Inject
    public ErisStreamSubscriber(ErisService erisService, ErisStreamRepository repository) {
        // Create a subscriber
        erisService.helloEvents().subscribe()
                // And subscribe to it with at least once processing semantics.
                .atLeastOnce(
                        // Create a flow that emits a Done for each message it processes
                        Flow.<ErisEvent>create().mapAsync(1, event -> {
                            if (event instanceof ErisEvent.GreetingMessageChanged) {
                                ErisEvent.GreetingMessageChanged messageChanged = (ErisEvent.GreetingMessageChanged) event;
                                // Update the message
                                return repository.updateMessage(messageChanged.getName(), messageChanged.getMessage());
                            } else {
                                // Ignore all other events
                                return CompletableFuture.completedFuture(Done.getInstance());
                            }
                        })
                );
    }
}
