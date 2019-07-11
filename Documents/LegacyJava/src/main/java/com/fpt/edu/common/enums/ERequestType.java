package com.fpt.edu.common.enums;

//    The request type indicates the type of request
public enum ERequestType {

//    If the borrower sends a request with type 1, the borrower is trying to make a request to borrow the book
    BORROWING(1),

//    If the borrower sends a request with type 2, the borrower is trying to make a request to return the book
    RETURNING(2);

    private int value;

    ERequestType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
