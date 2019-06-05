package com.fpt.edu.common;

public enum ETransferType {
    RETURNER(1),
    RECEIVER(2);

    private int value;

    private ETransferType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
