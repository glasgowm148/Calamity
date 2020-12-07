package org.cs2021.eris.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

import org.cs2021.eris.api.ErisService;

/**
 * The module that binds the ErisService so that it can be served.
 */
public class ErisModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindService(ErisService.class, ErisServiceImpl.class);
    }
}
