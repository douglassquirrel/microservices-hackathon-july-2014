package com.microserviceshack.rabbitmq;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microserviceshack.Config;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class EmitLogTopic {

  public static void main(String[] argv) {
    Connection connection = null;
    Channel channel = null;
    try {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost(Config.RABBIT_MQ_SERVER);
  
      connection = factory.newConnection();
      channel = connection.createChannel();

      channel.exchangeDeclare(Config.EXCHANGE, "topic");

      String routingKey = getRouting(argv);
      String message = getMessage(argv);

      channel.basicPublish(Config.EXCHANGE, routingKey, null, message.getBytes());
      System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");

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
  
  private static String getRouting(String[] strings){
//    if (strings.length < 1)
//    	    return "anonymous.info";
//    return strings[0];
	  return "ivan";
  }

  private static String getMessage(String[] strings) throws JsonGenerationException, JsonMappingException, IOException{
//    if (strings.length < 2)
//    	    return "Hello World!";
//    return joinStrings(strings, " ", 1);
	  Map<String, String> message = new HashMap<String, String>();
	  
	  message.put("Hello", "World!");
	  
	  ObjectMapper mapper = new ObjectMapper();
	  return mapper.writeValueAsString(message);
  }
  
//  private static String joinStrings(String[] strings, String delimiter, int startIndex) {
//    int length = strings.length;
//    if (length == 0 ) return "";
//    if (length < startIndex ) return "";
//    StringBuilder words = new StringBuilder(strings[startIndex]);
//    for (int i = startIndex + 1; i < length; i++) {
//        words.append(delimiter).append(strings[i]);
//    }
//    return words.toString();
//  }
}