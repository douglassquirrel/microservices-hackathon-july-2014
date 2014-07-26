package com.example;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class RoomService {

    private static final RoomRepository roomRepository = new RoomRepository();
    private static final Publisher publisher = new Publisher();

    public static void main(String[] a) throws IOException, SQLException {

        while (true) {
            System.out.println("1> Create Room");
            System.out.println("2> Create Door");
            System.out.println("3> Room names");
            System.out.println("4> Exit");

            String action = readLine("Choose one");

            String message;
            String topicName;
            switch (action) {
                case "1":
                    String roomName = readLine("Enter room name");
                    if (roomRepository.getRooms().contains(roomName)) {
                        System.out.println("Room already exists");
                        continue;
                    }
                    String description = readLine("Enter description");
                    message = createRoom(roomName, description);
                    topicName = "room_created";
                    break;
                case "2":
                    String firstRoom = readLine("Enter first room name");
                    if (!roomRepository.getRooms().contains(firstRoom)) {
                        System.out.println("Room does not exist");
                        continue;
                    }
                    String secondRoom = readLine("Enter second room name");
                    if (!roomRepository.getRooms().contains(secondRoom)) {
                        System.out.println("Room does not exist");
                        continue;
                    }
                    message = createDoor(firstRoom, secondRoom);
                    topicName = "door_created";
                    break;
                case "3":
                    System.out.println(roomRepository.getRooms());
                    continue;
                case "4":
                    return;
                default:
                    System.out.println("Unknown action: " + action);
                    continue;
            }
            publisher.publish(message, topicName);
        }
    }


    private static String readLine(final String prompt) {
        System.out.print(prompt + "> ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private static String createRoom(String roomName, String roomDescription) {
        return String.format("{\"room_name\": \"%s\", \"room_description\": \"%s\"}", roomName, roomDescription);
    }

    private static String createDoor(String room1Name, String room2Name) {
        return String.format("{\"room_one_name\": \"%s\", \"room_two_name\": \"%s\"}", room1Name, room2Name);
    }

}
