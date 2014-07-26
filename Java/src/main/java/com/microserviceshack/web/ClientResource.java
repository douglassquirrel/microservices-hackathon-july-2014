package com.microserviceshack.web;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/client")
public class ClientResource {

    @GET
    public ClientView client() {
        return new ClientView();
    }

}
