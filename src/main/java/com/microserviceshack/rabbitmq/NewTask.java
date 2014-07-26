package com.microserviceshack.rabbitmq;

import com.microserviceshack.Config;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

public class NewTask {
  

  public static void main(String[] argv) throws Exception {

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(Config.RABBIT_MQ_SERVER);
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    
//    channel.queueDeclare(Config.TASK_QUEUE_NAME, true, false, false, null);
 channel.queueDeclare(Config.TASK_QUEUE_NAME, false, false, false, null);

    String message = getMessage(argv);
    
    channel.basicPublish( "", Config.TASK_QUEUE_NAME, 
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                message.getBytes());
    System.out.println(" [x] Sent '" + message + "'");
    
    channel.close();
    connection.close();
  }
    
  private static String getMessage(String[] strings){
    if (strings.length < 1)
      return "Hello World!";
    return joinStrings(strings, " ");
  }  
  
  private static String joinStrings(String[] strings, String delimiter) {
    int length = strings.length;
    if (length == 0) return "";
    StringBuilder words = new StringBuilder(strings[0]);
    for (int i = 1; i < length; i++) {
      words.append(delimiter).append(strings[i]);
    }
    return words.toString();
  }
}