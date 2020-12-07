package org.cs2021.erisstream.impl;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import org.cs2021.eris.api.ErisService;
import org.cs2021.erisstream.api.ErisStreamService;

import javax.inject.Inject;

import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * Implementation of the ErisStreamService.
 */
public class ErisStreamServiceImpl implements ErisStreamService {
    private final ErisService erisService;
    private final ErisStreamRepository repository;

    @Inject
    public ErisStreamServiceImpl(ErisService erisService, ErisStreamRepository repository) {
        this.erisService = erisService;
        this.repository = repository;
    }

    @Override
    public ServiceCall<Source<String, NotUsed>, Source<String, NotUsed>> directStream() {
        return hellos -> completedFuture(
                hellos.mapAsync(8, name -> erisService.hello(name).invoke()));
    }

    @Override
    public ServiceCall<Source<String, NotUsed>, Source<String, NotUsed>> autonomousStream() {
        return hellos -> completedFuture(
                hellos.mapAsync(8, name -> repository.getMessage(name).thenApply(message ->
                        String.format("%s, %s!", message.orElse("Hello"), name)
                ))
        );
    }
}
