package com.microserviceshack.repository;

import com.microserviceshack.model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gonka on 26/07/2014.
 */
public class RoomManager {


    public Room getRoom(String roomid){
        try {
            Connection connection = connectToDatabaseOrDie();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT content->>'room_description' FROM facts WHERE content->>'room_name' = '" + roomid + "'");
            if(resultSet.next()){
                String result = resultSet.getString(1);
//                ObjectMapper mapper = new ObjectMapper();
//                Map<String,String> response = mapper.readValue(json, Map.class);
                return new Room(roomid, result );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("room not found!");
    }

    public List<Room> getAdjacentRooms(String roomid){
            List<Room> adjrooms = new ArrayList();
        try {
            Connection connection = connectToDatabaseOrDie();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT content->>'room_two_name' FROM facts WHERE topic='door_created' AND content->>'room_one_name' = '" + roomid + "'");
            while(resultSet.next()){
                adjrooms.add(getRoom(resultSet.getString(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adjrooms;
    }


    private static Connection connectToDatabaseOrDie() throws SQLException {
        Connection conn = null;
        try
        {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://microservices.cc9uedlzx2lk.eu-west-1.rds.amazonaws.com/micro";
            conn = DriverManager.getConnection(url, "microservices", "microservices");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.exit(2);
        }
        return conn;
    }


    /**
     * test
     */
    public static void main(String[] args) {
        RoomManager roomManager = new RoomManager();
        Room room_created = roomManager.getRoom("Atrium");
        System.out.println(room_created.room_description);
        //
        List<Room> garden = roomManager.getAdjacentRooms("Garden");
        System.out.println(garden);


    }


}
