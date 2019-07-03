package com.fpt.edu.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;
import java.util.TreeMap;

import static com.fpt.edu.constant.Constant.*;

public class BookMetadata {

	private Map<String, String> data;

	public BookMetadata() {
		this.data = new TreeMap<>();
		this.data.put(CURRENT_KEEPER, EMPTY_VALUE);
		this.data.put(BOOK_STATUS, EMPTY_VALUE);
		this.data.put(TX_TIMESTAMP, EMPTY_VALUE);
		this.data.put(REJECT_COUNT, String.valueOf(MIN_REJECT_COUNT));
		this.data.put(REJECT_REASON, EMPTY_VALUE);
		this.data.put(IMAGE_HASH, EMPTY_VALUE);
		this.data.put(IMAGE_LINK, EMPTY_VALUE);
		this.data.put(REJECTOR_EMAIL, EMPTY_VALUE);
	}

	// This constructor is used to get metadata from database
	public BookMetadata(
		String currentKeeper,
		String status
	) {
		String refreshBigchain = System.getenv("REFRESH_BIGCHAIN");
		boolean isRestoring = refreshBigchain == null ? false : true;
		if (!isRestoring) {
			this.data = new TreeMap<>();
			this.data.put(CURRENT_KEEPER, currentKeeper);
			this.data.put(BOOK_STATUS, status);
			this.data.put(TX_TIMESTAMP, EMPTY_VALUE);
			this.data.put(REJECT_COUNT, String.valueOf(MIN_REJECT_COUNT));
			this.data.put(REJECT_REASON, EMPTY_VALUE);
			this.data.put(IMAGE_HASH, EMPTY_VALUE);
			this.data.put(IMAGE_LINK, EMPTY_VALUE);
			this.data.put(REJECTOR_EMAIL, EMPTY_VALUE);
		} else {
			this.applyOldModel(currentKeeper, status, isRestoring);
		}
	}

	// This is for old model, used to refresh bigchain
	private void applyOldModel(
		String currentKeeper,
		String status,
		boolean isRestoring
	) {
		if (isRestoring) {
			this.data = new TreeMap<>();
			this.data.put(CURRENT_KEEPER, currentKeeper);
			this.data.put(BOOK_STATUS, status);
			this.data.put(TX_TIMESTAMP, EMPTY_VALUE);
			this.data.put(REJECT_COUNT, String.valueOf(MIN_REJECT_COUNT));
			this.data.put(REJECT_REASON, EMPTY_VALUE);
			this.data.put(IMAGE_HASH, EMPTY_VALUE);
		}
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
		this.data = new TreeMap<>();
		this.data.put(CURRENT_KEEPER, currentKeeper);
		this.data.put(BOOK_STATUS, status);
		this.data.put(TX_TIMESTAMP, transactionTimestamp);
		this.data.put(REJECT_COUNT, String.valueOf(rejectCount));
		this.data.put(REJECT_REASON, rejectReason);
		this.data.put(IMAGE_HASH, imgHash);
		this.data.put(IMAGE_LINK, imgUrl);
		this.data.put(REJECTOR_EMAIL, rejectorEmail);
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

	public void resetRejectCount() {
		this.data.put(REJECT_COUNT, String.valueOf(0));
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
