package com.fpt.edu.common;

public enum EBookTransferStatus {

	WAITING("transferring"),
	TRANSFERED("transferred");

	private String value;

	EBookTransferStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
