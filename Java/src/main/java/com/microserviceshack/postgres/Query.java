package com.microserviceshack.postgres;

import com.microserviceshack.Config;

import java.sql.*;
 
public class Query {
 
	public static void main(String[] argv) {
 
		System.out.println("-------- PostgreSQL "
				+ "JDBC Connection Testing ------------");
 
		try {
 
			Class.forName("org.postgresql.Driver");
 
		} catch (ClassNotFoundException e) {
 
			System.out.println("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
			e.printStackTrace();
			return;
 
		}
 
		System.out.println("PostgreSQL JDBC Driver Registered!");
 
		Connection connection = null;
 
		try {
 
			connection = DriverManager.getConnection(
//					"jdbc:postgresql:" + "//127.0.0.1:5432/" + "testdb", "mkyong",
					//"123456"
					"jdbc:postgresql://" + Config.POSTGRES_HOST + "/" + Config.POSTGRES_DATABASE, 
					Config.POSTGRES_USER,
					Config.POSTGRES_PASSWORD
					);
			
			Statement st = connection.createStatement();

			ResultSet rs = st.executeQuery("SELECT * FROM facts");
			while (rs.next())
			{
			   System.out.println("topic is " + rs.getString("topic") + 
					   ", timestamp is " + rs.getString(2) + ", content is " + rs.getString("content"));
			} 
			
			rs.close();
			st.close();
		} catch (SQLException e) {
 
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
 
		}
 
		if (connection != null) {
			System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}
	}
 
}