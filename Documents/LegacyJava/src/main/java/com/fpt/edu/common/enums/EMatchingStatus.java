package com.fpt.edu.common.enums;

// the matching status indicates the status of matching
public enum EMatchingStatus {

	//    By default, when the system found a borrow request and a return request that match to each other,
	//    1 matching instance will be created and  its status is 'paired'
	PAIRED(1),

	//When the 2 readers perform book transfer, the returner will send an Http Request to the system to tell the system to return the book,
	// the system will generate a pin code for the transfer, at this time the status of the matching will be transferred 'pending' to wait for the receiver to enter the pin code.
	PENDING(2),

	//When the returner enters the pin code correctly,
	// the system will perform a transfer transaction and the matching status will switch to "completed"
	CONFIRMED(3),

	// When user has already returned book manually, user can cancel returning request.
	CANCELED(4),

	// When receiver rejects to receive book, matching status is 'REJECTED'
	REJECTED(5),

	// When matching is made a period time, scheduler automatically updates matching to expired
	EXPIRED(6);

	private int value;

	EMatchingStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
