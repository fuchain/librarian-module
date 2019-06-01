package com.fpt.edu.common;

public enum RequestStatus {
    PENDING(1), MATCHING(2), COMPLETED(3);

    private int value;

    private RequestStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
