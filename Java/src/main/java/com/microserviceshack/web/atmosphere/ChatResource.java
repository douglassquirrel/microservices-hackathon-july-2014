package com.microserviceshack.web.atmosphere;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.microserviceshack.Movement;
import com.microserviceshack.model.Room;
import com.microserviceshack.repository.RoomManager;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
import org.atmosphere.annotation.Broadcast;
import org.atmosphere.annotation.Suspend;
import org.atmosphere.cpr.*;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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

    Map<String,String> locations = new HashMap<>();

    @Suspend(contentType = MediaType.APPLICATION_JSON)
    @GET
    public String suspend() {
        return "";
    }


    @Broadcast(writeEntity = false)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response broadcast(Message message, AtmosphereResource ar) throws IOException, InterruptedException {
        Connection connection = movement.getConnection();
        try {
            if (isValid(message.message)) {
                Command command = new Command(message.message);
                switch (command.command) {
                    case JOIN: {
                        movement.sendUserJoinedMessage(message.author);

                        return new Response(message.author, "You joined as: " + message.author);
                    }
                    case MOVE: {
                        movement.sendMoveMessage(message.author, command.arg1);

                        QueueingConsumer consumer = movement.getConsumer(connection);
                        Optional<String> optRoom = Optional.ofNullable(movement.getMovementResponse(consumer, message.author));

                        return new Response(message.author, optRoom.isPresent()?"Move to: " + optRoom.get():"Movement failed.");
                    }
                    case ROOM: {
                        Room room = roomManager.getRoom(command.arg1);
                        return new Response(message.author, room.id + " " + room.room_description);
                    }
                    case ADJACENT: {
                        List<Room> adjacentRooms = roomManager.getAdjacentRooms(command.arg1);
                        return new Response(message.author, String.format("Adjacent rooms to %s are %s", command.arg1, "" + adjacentRooms));
                    }
                    case HELP: {
                        String help = "";
                        CMD[] values = CMD.values();
                        for (CMD value : values) {
                            help += value.name().toLowerCase() + " ";
                        }
                        return new Response(message.author, help);
                    }
                }
            }
            return new Response(message.author, "You've got an error there :) " + message.message);
        } finally {
            connection.close();
        }
    }

    private enum CMD {
        MOVE,
        ROOM,
        ADJACENT,
        JOIN,
        HELP;
    }

    private boolean isValid(String cmd) {
        String[] split = cmd.split(" ");
        CMD[] values = CMD.values();
        for (CMD value : values) {
            if(value.name().toLowerCase().equals(split[0])) {
               return true;
            }
        }
        return false;
    }

    private class Command {
        CMD command;
        String arg1;
        String arg2;

        private Command(String cmd) {
            String[] split = cmd.split(" ");
            for (CMD value : CMD.values()) {
                if(value.name().toLowerCase().equals(split[0])) {
                    command = value;
                }
            }
            if(split.length>1) {
                arg1 = split[1];
            }
            if(split.length>2) {
                arg2 = split[2];
            }

        }
    }
}
