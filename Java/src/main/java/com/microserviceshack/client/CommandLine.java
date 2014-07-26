package com.microserviceshack.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.microserviceshack.Config;
import com.microserviceshack.Movement;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

public class CommandLine {

	public static void main(String[] args) {
		CommandLine line = new CommandLine();
		line.process(line);
	}

	private void process(CommandLine line) {
		try {

			Movement movement = new Movement();
			Connection connection = movement.getConnection();
			QueueingConsumer consumer = movement.getConsumer(connection);

			String user_id = getUserName();
			movement.sendUserJoinedMessage(user_id);

			movement.getMovementResponse(consumer, user_id);
			// Need to get room details

			while (true) {
				String room = getNextRoom();
				movement.sendMoveMessage(user_id, room);
				movement.getMovementResponse(consumer, user_id);
				// Need to check OK
				// Need to get room details
				// displayRoomDetails(connection, room);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean getResponse(Movement movement) throws InterruptedException {
		// new ReceiveMovement().getMessage();;
		return true;
	}

	private void displayRoomDetails(Connection connection, String room) {
		System.out.println("You are in " + room);
	}

	private String getNextRoom() throws IOException {
		System.out.print("Enter next room:");
		return getInput();
	}

	public QueueingConsumer getConsumer(Connection connection)
			throws IOException {
		Channel channel;
		channel = connection.createChannel();

		channel.exchangeDeclare(Config.EXCHANGE, "topic");
		String queueName = channel.queueDeclare().getQueue();

		bindQueue(channel, queueName);

		// System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		QueueingConsumer consumer = getConsumer(channel, queueName);
		return consumer;
	}

	private String getUserName() throws IOException {
		System.out.print("Enter your username:");
		return getInput();
	}

	private String getInput() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input = br.readLine();
		return input;
	}

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
}
