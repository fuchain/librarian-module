package com.fpt.edu.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;
import java.util.TreeMap;

import static com.fpt.edu.constant.Constant.*;

public class BookMetadata {

	private Map<String, String> data;

	public BookMetadata() {
		String refreshBigchain = System.getenv("REFRESH_BIGCHAIN"); // TRUE/FALSE
		boolean isRestoring =  refreshBigchain == null ? false : Boolean.valueOf(refreshBigchain);
		if (isRestoring) {
			this.applyOldMetadataModel();
		} else {
			this.applyLatestMetadataModel();
		}
	}

	// This constructor is used to get metadata from database
	public BookMetadata(String currentKeeper, String status) {
		this.applyLatestMetadataModel();
	}

	// This constructor is used to get metadata from blockchain
	public BookMetadata(
		String currentKeeper,
		String status,
		String transactionTimestamp,
		int rejectCount,
		String rejectReason,
		String imgHash,
		String imgUrl,
		String rejectorEmail
	) {
		this.applyOldMetadataModel();
		this.data.put(REJECTOR_EMAIL, rejectorEmail);
	}

	// Old model doesn't have email of rejector
	private void applyOldMetadataModel() {
		this.data = new TreeMap<>();
		this.data.put(CURRENT_KEEPER, EMPTY_VALUE);
		this.data.put(BOOK_STATUS, EMPTY_VALUE);
		this.data.put(TX_TIMESTAMP, EMPTY_VALUE);
		this.data.put(REJECT_COUNT, String.valueOf(MIN_REJECT_COUNT));
		this.data.put(REJECT_REASON, EMPTY_VALUE);
		this.data.put(IMAGE_HASH, EMPTY_VALUE);
		this.data.put(IMAGE_LINK, EMPTY_VALUE);
	}

	private void applyLatestMetadataModel() {
		this.applyOldMetadataModel();
		this.data.put(REJECTOR_EMAIL, EMPTY_VALUE);
	}

	public Map<String, String> getData() {
		return this.data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	@JsonIgnore
	public String getCurrentKeeper() {
		String currentKeeper = this.data.get(CURRENT_KEEPER);
		return currentKeeper == null ? EMPTY_VALUE : currentKeeper;
	}

	public void setCurrentKeeper(String currentKeeper) {
		this.data.put(CURRENT_KEEPER, currentKeeper);
	}

	@JsonIgnore
	public String getStatus() {
		String status = this.data.get(BOOK_STATUS);
		return status == null ? EMPTY_VALUE : status;
	}

	public void setStatus(String status) {
		this.data.put(BOOK_STATUS, status);
	}

	@JsonIgnore
	public String getTransactionTimestamp() {
		String txTimestamp = this.data.get(TX_TIMESTAMP);
		return txTimestamp == null ? EMPTY_VALUE : txTimestamp;
	}

	public void setTransactionTimestamp(String transactionTimestamp) {
		this.data.put(TX_TIMESTAMP, transactionTimestamp);
	}

	public void increaseLastRejectCount() {
		this.data.put(REJECT_COUNT, String.valueOf(this.getRejectCount() + 1));
	}

	@JsonIgnore
	public int getRejectCount() {
		String rejectCount = this.data.get(REJECT_COUNT);
		return rejectCount == null ? MIN_REJECT_COUNT : Integer.parseInt(rejectCount);
	}

	@JsonIgnore
	public boolean isLastRejectCountOver() {
		return this.getRejectCount() > MAX_REJECT_COUNT;
	}

	@JsonIgnore
	public String getRejectReason() {
		String reason = this.data.get(REJECT_REASON);
		return reason == null ? EMPTY_VALUE : reason;
	}

	public void setRejectReason(String rejectReason) {
		this.data.put(REJECT_REASON, rejectReason);
	}

	@JsonIgnore
	public String getImgHash() {
		String imgHash = this.data.get(IMAGE_HASH);
		return imgHash == null ? EMPTY_VALUE : imgHash;
	}

	public void setImgHash(String imgHash) {
		this.data.put(IMAGE_HASH, imgHash);
	}

	@JsonIgnore
	public String getRejectImageLink() {
		String imageLink = this.data.get(IMAGE_LINK);
		return imageLink == null ? EMPTY_VALUE : imageLink;
	}

	public void setImageLink(String url) {
		this.data.put(IMAGE_LINK, url);
	}

	@JsonIgnore
	public String getRejectorEmail() {
		String rejectorEmail = this.data.get(REJECTOR_EMAIL);
		return rejectorEmail == null ? EMPTY_VALUE : rejectorEmail;
	}

	public void setRejectorEmail(String rejectorEmail) {
		this.data.put(REJECTOR_EMAIL, rejectorEmail);
	}
}
