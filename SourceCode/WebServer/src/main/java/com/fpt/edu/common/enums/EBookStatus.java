package com.fpt.edu.common.enums;

public enum EBookStatus {

	IN_USE("in use"),
	DAMAGED("damanged");

	private String value;

	EBookStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
