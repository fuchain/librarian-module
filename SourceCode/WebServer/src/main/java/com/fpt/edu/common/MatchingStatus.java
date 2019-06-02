package com.fpt.edu.common;

public enum MatchingStatus {
    PAIRED(1), PENDING(2), CONFIRMED(3);

    private int value;

    private MatchingStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
