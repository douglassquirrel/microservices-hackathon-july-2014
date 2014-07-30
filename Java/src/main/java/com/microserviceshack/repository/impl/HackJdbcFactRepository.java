package com.microserviceshack.repository.impl;

import com.microserviceshack.Config;
import com.microserviceshack.model.Fact;
import com.microserviceshack.repository.FactRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by paul on 26/07/2014.
 */
public class HackJdbcFactRepository implements FactRepository {
    @Override
    public List<Fact> find(String topic) {

        if (topic == null || topic.length() == 0) {
            throw new IllegalArgumentException("Invalid topic param to find facts");
        }

        // TODO - make this nice :)

        try {

            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {

            System.out.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();
            return Collections.EMPTY_LIST;

        }

        System.out.println("PostgreSQL JDBC Driver Registered!");

        Connection connection = null;

        List<Fact> facts = new ArrayList<Fact>();

        try {

            connection = DriverManager.getConnection(
//					"jdbc:postgresql:" + "//127.0.0.1:5432/" + "testdb", "mkyong",
                    //"123456"
                    "jdbc:postgresql://" + Config.POSTGRES_HOST + "/" + Config.POSTGRES_DATABASE,
                    Config.POSTGRES_USER,
                    Config.POSTGRES_PASSWORD
            );

            Statement st = connection.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM facts WHERE topic = '" + topic + "'");
            while (rs.next())
            {
                Fact fact = new Fact();
                fact.setTopic(rs.getString("topic"));
                fact.setTimeStamp(rs.getTimestamp("ts"));
                fact.setContent(rs.getString("content"));

                System.out.println("topic is " + rs.getString("topic") +
                        ", timestamp is " + rs.getString(2) + ", content is " + rs.getString("content"));
            }

            rs.close();
            st.close();
        } catch (SQLException e) {

            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return Collections.EMPTY_LIST;

        }

        if (connection != null) {
            System.out.println("You made it, take control your database now!");
        } else {
            System.out.println("Failed to make connection!");
        }

        return facts;
    }
}
