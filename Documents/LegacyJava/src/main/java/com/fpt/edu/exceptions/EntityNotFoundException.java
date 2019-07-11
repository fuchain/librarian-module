package com.fpt.edu.exceptions;

public class EntityNotFoundException extends Exception {
    private long id;
    private String message;

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(long id, String message) {
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
