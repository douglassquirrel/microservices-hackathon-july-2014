package com.microserviceshack.web;

import com.microserviceshack.web.atmosphere.ChatResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.atmosphere.cpr.AtmosphereServlet;

public class ClientApplication extends Application<ClientConfiguration> {

    public static void main(String[] args) throws Exception {
        new ClientApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<ClientConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets/", "/assets"));
        bootstrap.addBundle(new ViewBundle());
    }

    @Override
    public void run(ClientConfiguration clientConfiguration, Environment environment) throws Exception {
        AtmosphereServlet atmosphereServlet = new AtmosphereServlet();
        atmosphereServlet.framework().addInitParameter("com.sun.jersey.config.property.packages", "com.microserviceshack.web.atmosphere");
        atmosphereServlet.framework().addInitParameter("org.atmosphere.websocket.messageContentType", "application/json");
        atmosphereServlet.framework().addInitParameter("org.atmosphere.cpr.broadcastFilterClasses", "com.microserviceshack.web.atmosphere.BadWordFilter");
        environment.servlets().addServlet("atmosphere",atmosphereServlet).addMapping("/chat/*");


        final ClientResource resource = new ClientResource();
        environment.jersey().register(resource);

        final ChatResource chatResource = new ChatResource();
        environment.jersey().register(chatResource);
    }
}
