package com.fpt.edu.common.enums;

public enum ERole {

	LIBRARIAN(1),
	READER(2);

	private int value;

	ERole(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}
}
