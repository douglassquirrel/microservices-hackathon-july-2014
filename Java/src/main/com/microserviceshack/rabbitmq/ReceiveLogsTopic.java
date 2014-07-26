package com.microserviceshack.rabbitmq;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import com.microserviceshack.Config;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

public class ReceiveLogsTopic {

  public static void main(String[] argv) {
    Connection connection = null;
    Channel channel = null;
    try {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost(Config.RABBIT_MQ_SERVER);
  
      connection = factory.newConnection();
      channel = connection.createChannel();

      channel.exchangeDeclare(Config.EXCHANGE, "topic");
      String queueName = channel.queueDeclare().getQueue();
 
      if (argv.length < 1){
        System.err.println("Usage: ReceiveLogsTopic [binding_key]...");
        System.exit(1);
      }
    
      for(String bindingKey : argv){    
        channel.queueBind(queueName, Config.EXCHANGE, bindingKey);
      }
    
      System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

      QueueingConsumer consumer = new QueueingConsumer(channel);
      channel.basicConsume(queueName, true, consumer);

      while (true) {
        QueueingConsumer.Delivery delivery = consumer.nextDelivery();
        String message = new String(delivery.getBody());
        String routingKey = delivery.getEnvelope().getRoutingKey();

        System.out.println(" [x] Received '" + routingKey + "':'" + message + "'");
  	  Map<String, String> map = new HashMap<String, String>();

  	  try
  	  {
	  	  ObjectMapper mapper = new ObjectMapper();
	  	  Map response = mapper.readValue(message, Map.class);  
	  	  for (Object key : response.keySet()) {
	  		 System.out. println("Key is " + key + " value is " + response.get(key));
	  	  }
  	  }
  	  catch (Exception e)
  	  {
  	  }
      }
    }
    catch  (Exception e) {
      e.printStackTrace();
    }
    finally {
      if (connection != null) {
        try {
          connection.close();
        }
        catch (Exception ignore) {}
      }
    }
  }
}