package com.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.Test;

public class QueueTest {
    private static final String QUEUE_NAME = "alex2";

    @Test
    public void testQueue() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("54.76.183.35");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        String message = "{\"sku\": \"1245\", \"name\": \"ZZZZZZZ\", \"price\": \"12.45\"}";
        channel.basicPublish(QUEUE_NAME, "room_created", null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");
        channel.close();
        connection.close();
    }


}
