package com.microserviceshack;

public class Config {

	// Rabbit MQ Messaging
	
	public static final String RABBIT_MQ_SERVER = "54.76.183.35";
//	public static final String TASK_QUEUE_NAME = "task_queue";
//	public static final String RABBIT_MQ_SERVER = "localhost";
	public static final String TASK_QUEUE_NAME = "gonka"; //"task_queue";
	
	public static final String EXCHANGE = "alex2";

	// Postgres Database
	
	public static final String POSTGRES_HOST = "microservices.cc9uedlzx2lk.eu-west-1.rds.amazonaws.com";
	public static final String POSTGRES_DATABASE ="micro";
	public static final String POSTGRES_USER = "microservices";
	public static final String POSTGRES_PASSWORD = "microservices";
}
