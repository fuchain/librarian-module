package com.fpt.edu.common.enums;

public enum ETransactionStatus {

	PENDING(1),
	COMPLETED(2);

	private int value;

	ETransactionStatus(int value) {
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}
}
