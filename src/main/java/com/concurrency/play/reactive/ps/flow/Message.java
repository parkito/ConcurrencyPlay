package com.concurrency.play.reactive.ps.flow;

public class Message {

    private String message;

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message=" + message +
                '}';
    }
}
