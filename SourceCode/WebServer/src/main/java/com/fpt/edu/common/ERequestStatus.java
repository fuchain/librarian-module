package com.fpt.edu.common;

public enum ERequestStatus {
    PENDING(1), MATCHING(2),COMPLETED(3);

    private int value;

    private ERequestStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
