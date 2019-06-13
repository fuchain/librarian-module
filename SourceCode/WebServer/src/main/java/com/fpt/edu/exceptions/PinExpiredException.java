package com.fpt.edu.exceptions;

public class PinExpiredException extends Exception {
    private long id;
    private String message;

    public PinExpiredException(String message) {
        super(message);
    }

    public PinExpiredException(long id, String message) {
        this.id = id;
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
