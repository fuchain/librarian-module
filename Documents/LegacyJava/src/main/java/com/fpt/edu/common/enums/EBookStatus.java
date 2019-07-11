package com.fpt.edu.common.enums;

public enum EBookStatus {

	IN_USE("in use"),
	LOCKED("locked"),
	DAMAGED("damaged");

	private String value;

	EBookStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
