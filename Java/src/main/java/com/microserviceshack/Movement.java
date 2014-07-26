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
import com.rabbitmq.client.QueueingConsumer;

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

	public boolean getResponse() {
		// TODO Auto-generated method stub
		return false;
	}

	// public void getMessage(QueueingConsumer consumer)
	// throws InterruptedException {
	// System.out.println("Waiting");
	// QueueingConsumer.Delivery delivery = consumer.nextDelivery();
	// String message = new String(delivery.getBody());
	// String routingKey = delivery.getEnvelope().getRoutingKey();
	//
	// System.out.println(" [x] Received '" + routingKey + "':'" + message
	// + "'");
	// Map<String, String> map = new HashMap<String, String>();
	//
	// try {
	//
	// ObjectMapper mapper = new ObjectMapper();
	// Map response = mapper.readValue(message, Map.class);
	// for (Object key : response.keySet()) {
	// System.out.println("Key is " + key + " value is "
	// + response.get(key));
	// }
	// } catch (Exception e) {
	// }
	// }
	//
	private QueueingConsumer getConsumer(Channel channel, String queueName)
			throws IOException {
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(queueName, true, consumer);
		return consumer;
	}

	private void bindQueue(Channel channel, String queueName)
			throws IOException {
		channel.queueBind(queueName, Config.EXCHANGE, "#");
	}

	public QueueingConsumer getConsumer(Connection connection)
			throws IOException {
		Channel channel;
		channel = connection.createChannel();

		channel.exchangeDeclare(Config.EXCHANGE, "topic");
		String queueName = channel.queueDeclare().getQueue();

		bindQueue(channel, queueName);

		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		QueueingConsumer consumer = getConsumer(channel, queueName);
		return consumer;
	}

	public Message getSingleMessage(QueueingConsumer consumer)
			throws IOException, InterruptedException {

		QueueingConsumer.Delivery delivery = consumer.nextDelivery();
		String message = new String(delivery.getBody());

		Map response = null;
		String routingKey = delivery.getEnvelope().getRoutingKey();
		Message result = new Message();
		result.setTopic(routingKey);

		System.out.println(" [x] Received '" + routingKey + "':'" + message
				+ "'");

		try {
			ObjectMapper mapper = new ObjectMapper();
			response = mapper.readValue(message, Map.class);
			result.setDetails(response);
			for (Object key : response.keySet()) {
				System.out.println("Key is " + key + " value is "
						+ response.get(key));
			}
		} catch (Exception e) {
		}
		return result;
	}

	public String getMovementResponse(QueueingConsumer consumer, String user_id)
			throws IOException, InterruptedException {
		boolean response = false;
		String room_name = null;
		while (!response) {
			Message message = getSingleMessage(consumer);
			System.out.println(message.getTopic());
			response = message.getTopic().equals("movement_successful");

			response = response
					&& message.getDetails().get("user_id").equals(user_id);
			if (response) {
				room_name = (String) message.getDetails().get("room_name");
				System.out.println("Room is " + room_name);
			} else {
				response = message.getTopic().equals("movement_failed");
			}
		}

		return room_name;
	}

}
