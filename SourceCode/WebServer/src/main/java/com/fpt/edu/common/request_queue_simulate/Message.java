package com.fpt.edu.common.request_queue_simulate;

public class Message {
    private String action;
    private Object message;

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
