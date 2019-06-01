package com.fpt.edu.common;

public enum RequestStatus {
    PENDING(1), MATCHING(2);

    private int value;

    private RequestStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
