package com.microserviceshack.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microserviceshack.web.atmosphere.ChatResource;
import com.microserviceshack.web.atmosphere.Message;
import com.microserviceshack.web.atmosphere.Response;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/client")
public class ClientResource {



    @GET
    public void client(@QueryParam("user") String user) throws JsonProcessingException {

        Broadcaster broadcaster = ChatResource.listeners.get(user);
        String msg = new ObjectMapper().writeValueAsString(new Response("admin", "Hello!"));
        broadcaster.broadcast(msg);

    }


}
