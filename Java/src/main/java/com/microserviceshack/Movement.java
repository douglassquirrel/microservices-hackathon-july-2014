package com.microserviceshack;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Movement {

	public Connection getConnection() throws IOException {
		Connection connection;
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(Config.RABBIT_MQ_SERVER);
		connection = factory.newConnection();
		return connection;
	}

	public Channel getPublisherChannel(Connection connection)
			throws IOException {
		Channel channel;
		channel = connection.createChannel();
		channel.exchangeDeclare(Config.EXCHANGE, "topic");
		return channel;
	}

	public void sendMoveMessage(String user_id, String room)
			throws JsonGenerationException, JsonMappingException, IOException {

		Connection connection = null;
		try {
			connection = getConnection();
			Channel channel = getPublisherChannel(connection);

			Map<String, String> message = new HashMap<String, String>();
			message.put("user_id", user_id);
			message.put("room_id", room);
			sendMessage(channel, "user_intended_to_move", message);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception ignore) {
				}
			}
		}
	}

	public void sendUserJoinedMessage(String user_id)
			throws JsonGenerationException, JsonMappingException, IOException {

		Connection connection = null;
		try {
			connection = getConnection();
			Channel channel = getPublisherChannel(connection);

			Map<String, String> message = new HashMap<String, String>();
			message.put("user_id", user_id);
			sendMessage(channel, "user_joined", message);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception ignore) {
				}
			}
		}

	}

	private void sendMessage(Channel channel, String routingKey, Object message)
			throws JsonGenerationException, JsonMappingException, IOException {

		ObjectMapper mapper = new ObjectMapper();
		String string = mapper.writeValueAsString(message);
		channel.basicPublish(Config.EXCHANGE, routingKey, null,
				string.getBytes());
		System.out.println(" [x] Sent '" + routingKey + "':'" + string + "'");
	}

}
