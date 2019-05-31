package com.fpt.edu.exception;

public class StatusNotSupportedException extends Exception {
    private Long id;
    private String message;

    public StatusNotSupportedException(String message) {
        super(message);
    }

    public StatusNotSupportedException(Long id, String message) {
        this.id = id;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
