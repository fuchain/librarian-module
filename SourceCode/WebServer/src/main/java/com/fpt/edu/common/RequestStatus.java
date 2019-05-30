package com.fpt.edu.common;

public enum RequestStatus {
    REQUIRING(1), RETURNING(2);

    private int value;

    private RequestStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
