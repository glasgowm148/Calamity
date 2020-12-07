package org.cs2021.erisstream.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

import org.cs2021.eris.api.ErisService;
import org.cs2021.erisstream.api.ErisStreamService;

/**
 * The module that binds the ErisStreamService so that it can be served.
 */
public class ErisStreamModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        // Bind the ErisStreamService service
        bindService(ErisStreamService.class, ErisStreamServiceImpl.class);
        // Bind the ErisService client
        bindClient(ErisService.class);
        // Bind the subscriber eagerly to ensure it starts up
        bind(ErisStreamSubscriber.class).asEagerSingleton();
    }
}
