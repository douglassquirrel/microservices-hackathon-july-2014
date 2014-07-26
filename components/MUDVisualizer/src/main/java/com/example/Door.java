package com.example;

import java.util.Date;

public class Door {

    private Date timestamp;
    private String room1;
    private String room2;

    public Door(String room1, String room2, Date timestamp) {
        this.timestamp = timestamp;
        this.room1 = room1;
        this.room2 = room2;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getRoom1() {
        return room1;
    }

    public void setRoom1(String room1) {
        this.room1 = room1;
    }

    public String getRoom2() {
        return room2;
    }

    public void setRoom2(String room2) {
        this.room2 = room2;
    }
}
