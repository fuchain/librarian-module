package com.fpt.edu.common;

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
