package com.fpt.edu.common;

// The transfer type indicates the type of transfer
public enum ETransferType {

    // If the returner returns a book, the type of transfer is "retuner",
    RETURNER(1),

    // If the borrower receives a book, the type of transfer is "receiver".
    RECEIVER(2);

    private int value;

    private ETransferType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
