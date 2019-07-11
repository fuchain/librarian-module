package com.fpt.edu.exceptions;

public class NotFoundException extends Exception {
    private String messString;


    public NotFoundException(String message) {
        super(message);
        this.messString = messString;
    }

    public String getMessString() {
        return messString;
    }

    public void setMessString(String messString) {
        this.messString = messString;
    }
}
