package com.example.final_project.entity;

import java.io.Serializable;

public class Message implements Serializable {
    String message;
    int x;
    int y;

    public Message(String message, int x, int y) {
        this.message = message;
        this.x = x;
        this.y = y;
    }

    public String getMessage() {
        return message;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
