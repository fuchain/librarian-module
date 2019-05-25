package com.fpt.edu.common;

public enum ETransactionStatus {
	SUNDAY(1), MONDAY(2), TUESDAY(3), WEDNESDAY(4),
    THURSDAY(5), FRIDAY(6), SATURDAY(7);

	private int value;

	private ETransactionStatus(int value) {
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}
}
