package com.microserviceshack;

import java.util.Map;

public class Message {
	private String topic;
	private Map details;

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Map getDetails() {
		return details;
	}

	public void setDetails(Map details) {
		this.details = details;
	}
}
