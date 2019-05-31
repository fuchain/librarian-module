package com.fpt.edu.common;

public enum ETransactionStatus {
	PENDING(1),COMPLETED(2);

	private int value;

	private ETransactionStatus(int value) {
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}
}
