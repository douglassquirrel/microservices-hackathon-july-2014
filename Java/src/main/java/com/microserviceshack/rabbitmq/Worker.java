package com.microserviceshack.rabbitmq;

import com.microserviceshack.Config;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
  
public class Worker {

  public static void main(String[] argv) throws Exception {

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(Config.RABBIT_MQ_SERVER);
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    
//    channel.queueDeclare(Config.TASK_QUEUE_NAME, true, false, false, null);
  channel.queueDeclare(Config.TASK_QUEUE_NAME, false, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    
    channel.basicQos(1);
    
    QueueingConsumer consumer = new QueueingConsumer(channel);
    channel.basicConsume(Config.TASK_QUEUE_NAME, false, consumer);
    
    while (true) {
      QueueingConsumer.Delivery delivery = consumer.nextDelivery();
      String message = new String(delivery.getBody());
      
      System.out.println(" [x] Received '" + message + "'");
      doWork(message);
      System.out.println(" [x] Done");

      channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    }         
  }
  
  private static void doWork(String task) throws InterruptedException {
    for (char ch: task.toCharArray()) {
      if (ch == '.') Thread.sleep(1000);
    }
  }
}