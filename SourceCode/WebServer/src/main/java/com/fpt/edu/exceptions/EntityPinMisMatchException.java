package com.fpt.edu.exceptions;

public class EntityPinMisMatchException extends Exception {
    private long id;
    private String message;

    public EntityPinMisMatchException(String message) {
        super(message);
    }

    public EntityPinMisMatchException(long id, String message) {
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
