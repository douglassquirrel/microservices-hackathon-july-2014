package com.microserviceshack.repository;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microserviceshack.model.Room;

import java.io.IOException;
import java.sql.*;
import java.util.Map;

/**
 * Created by gonka on 26/07/2014.
 */
public class RoomManager {


    public Room getRoom(String roomid){
        try {
            Connection connection = connectToDatabaseOrDie();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM facts WHERE topic = '" + roomid + "'");
            if(resultSet.next()){
                String json = resultSet.getString(3);
                ObjectMapper mapper = new ObjectMapper();
                Map<String,String> response = mapper.readValue(json, Map.class);
                return new Room(resultSet.getString(1), response.get("room_description") );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
        Room room_created = new RoomManager().getRoom("room_created");
        System.out.println(room_created.room_description);
    }


}
