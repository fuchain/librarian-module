package com.fpt.edu.exceptions;

public class EntityIdMismatchException extends Exception {
    private long id;
    private String message;

    public EntityIdMismatchException(String message) {
        super(message);
    }

    public EntityIdMismatchException(long id, String message) {
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
