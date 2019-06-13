package com.fpt.edu.common.enums;

public enum ERequestMode {
	// When user chooses how to pair automatically, the borrowed and returned requests returned requests will be
	// automatically paired by the system.
	AUTOMATIC(1),

	//When user chooses how to search manually, the returner must find the borrowers by themselves
	// without the system support that looking for borrower requests.
	MANUAL(2);

	private int value;

	ERequestMode(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
