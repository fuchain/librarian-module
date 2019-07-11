package com.fpt.edu.common.enums;

// The request status indicates the status of request
public enum ERequestStatus {

    // By default, when the reader makes a request to borrow or return a book, the status of the request is "pending",
    // it means that the request is waiting for the system to match the borrowed and returned requests.
    PENDING(1),

    // After the system found a pair of a borrowed request and a returned request,
    // the status of the request will be switched to "matching",
    // it means that the requests have been matched. Now the borrower and returner have already known each other.
    MATCHING(2),

    // When the borrower and returner are making the transfer, if the transaction transfer is created,
    // the status of the request will be converted to "completed".
    COMPLETED(3),

	// When user has already returned book manually, user can cancel returning request.
    CANCELED(4),

	// When request is made a period time, scheduler automatically updates request to expired
    EXPIRED(5);

    private int value;

    ERequestStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
