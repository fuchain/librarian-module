package com.fpt.edu.exceptions;

public class TypeNotSupportedException extends Exception {
    private Long id;
    private String message;

    public TypeNotSupportedException(String message) {
        super(message);
    }

    public TypeNotSupportedException(Long id, String message) {
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
