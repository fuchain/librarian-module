package com.fpt.edu.common;

public enum RequestType {
    BORROWING(1), RETURNING(2);

    private int value;

    private RequestType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
