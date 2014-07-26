package com.microserviceshack.repository;

import com.microserviceshack.model.Room;

import java.sql.*;

/**
 * Created by gonka on 26/07/2014.
 */
public class RoomManager {


    Room getRoom(String roomid){
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
        Room room_created = new RoomManager().getRoom("Atrium");
        System.out.println(room_created.room_description);
    }


}
