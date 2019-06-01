package com.fpt.edu.common;

public enum ERequestType {
    BORROWING(1), RETURNING(2);

    private int value;

    private ERequestType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
