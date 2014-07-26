package com.microserviceshack.model;

import java.util.Date;

/**
 * Created by paul on 26/07/2014.
 */
public class Fact {

    private String topic;
    private Date timeStamp;
    private String content;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
