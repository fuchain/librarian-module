package com.fpt.edu.common;

public enum EBookTransferStatus {

	WAITING("waiting"),
	TRANSFERED("transfered");

	private String value;

	EBookTransferStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
