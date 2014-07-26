package com.microserviceshack.web.atmosphere;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.microserviceshack.Movement;
import com.microserviceshack.model.Room;
import com.microserviceshack.repository.RoomManager;
import org.atmosphere.annotation.Broadcast;
import org.atmosphere.annotation.Suspend;
import org.atmosphere.cpr.*;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: coder
 * Date: 4/8/13
 * Time: 11:20 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/")
public class ChatResource {

    Movement movement = new Movement();
    RoomManager roomManager = new RoomManager();

    public static Map<String,Broadcaster> listeners = new HashMap<String,Broadcaster>();

    @Suspend(contentType = MediaType.APPLICATION_JSON)
    @GET
    public String suspend() {
        return "";
    }


    @Broadcast(writeEntity = false)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response broadcast(Message message, AtmosphereResource ar) throws IOException {
        if(!listeners.containsKey(message.author)) {
            Broadcaster broadcaster = BroadcasterFactory.getDefault().lookup("/*");
            broadcaster.addBroadcasterListener(new BroadcasterListenerAdapter());
            listeners.put(message.author,broadcaster);
        }

        if(message.message.startsWith("join")) {
            movement.sendUserJoinedMessage(message.author);
            return new Response(message.author,"You joined as: "+message.author);
        } else if (message.message.startsWith("move")) {
            String room = message.message.substring("move ".length());
            movement.sendMoveMessage(message.author, room);
            return new Response(message.author,"Move to: "+room);
        } else if(message.message.startsWith("room")) {
            Room room = roomManager.getRoom(message.message.substring("room ".length()));
            return new Response(message.author, room.id+" "+room.room_description);
        }
        return new Response(message.author, message.message);
    }
}
