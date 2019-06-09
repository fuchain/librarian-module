package com.fpt.edu.common;

public enum EBookTransferStatus {

	TRANSFERRING("transferring"),
	TRANSFERRED("transferred");

	private String value;

	EBookTransferStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
