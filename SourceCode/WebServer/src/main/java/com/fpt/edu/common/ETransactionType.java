package com.fpt.edu.common;

public enum ETransactionType {
	TRANSFERED(1),

	REJECTED(2);

	private int value;

	ETransactionType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
