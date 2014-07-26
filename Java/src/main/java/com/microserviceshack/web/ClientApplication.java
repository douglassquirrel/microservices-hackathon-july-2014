package com.microserviceshack.web;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;

public class ClientApplication extends Application<ClientConfiguration> {

    public static void main(String[] args) throws Exception {
        new ClientApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<ClientConfiguration> bootstrap) {
        bootstrap.addBundle(new ViewBundle());
    }

    @Override
    public void run(ClientConfiguration clientConfiguration, Environment environment) throws Exception {
        final ClientResource resource = new ClientResource();
        environment.jersey().register(resource);
    }
}
