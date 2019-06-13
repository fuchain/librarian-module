package com.fpt.edu.entities;

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
		this.data.put(REJECT_COUNT, String.valueOf(BC_MIN_REJECT_COUNT));
		this.data.put(REJECT_REASON, EMPTY_VALUE);
		this.data.put(IMAGE_HASH, EMPTY_VALUE);
	}

	// This constructor is used to get metadata from database
	public BookMetadata(
		String currentKeeper,
		String status
	) {
		this.data = new TreeMap<>();
		this.data.put(CURRENT_KEEPER, currentKeeper);
		this.data.put(BOOK_STATUS, status);
		this.data.put(TX_TIMESTAMP, EMPTY_VALUE);
		this.data.put(REJECT_COUNT, String.valueOf(BC_MIN_REJECT_COUNT));
		this.data.put(REJECT_REASON, EMPTY_VALUE);
		this.data.put(IMAGE_HASH, EMPTY_VALUE);
	}

	// This constructor is used to get metadata from blockchain
	public BookMetadata(
		String curerntKeeper,
		String status,
		String transactionTimestamp,
		int rejectCount,
		String rejectReason,
		String imgHash
	) {
		this.data = new TreeMap<>();
		this.data.put(CURRENT_KEEPER, curerntKeeper);
		this.data.put(BOOK_STATUS, status);
		this.data.put(TX_TIMESTAMP, transactionTimestamp);
		this.data.put(REJECT_COUNT, String.valueOf(rejectCount));
		this.data.put(REJECT_REASON, rejectReason);
		this.data.put(IMAGE_HASH, imgHash);
	}

	public Map<String, String> getData() {
		return this.data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	public void setCurrentKeeper(String currentKeeper) {
		this.data.put(CURRENT_KEEPER, currentKeeper);
	}

	public void setStatus(String status) {
		this.data.put(BOOK_STATUS, status);
	}

	public void setTransactionTimestamp(String transactionTimestamp) {
		this.data.put(TX_TIMESTAMP, transactionTimestamp);
	}

	public void increaseLastRejectCount() {
		this.data.put(REJECT_COUNT, String.valueOf(this.getRejectCount() + 1));
	}

	public int getRejectCount() {
		String count = this.data.get(REJECT_COUNT);
		if (count == null) {
			return 0;
		}
		return Integer.parseInt(count);
	}

	public void setRejectCount(int rejectCount) {
		this.data.put(REJECT_COUNT, String.valueOf(rejectCount));
	}

	public boolean isLastRejectCountOver() {
		return this.getRejectCount() >= BC_MAX_REJECT_COUNT;
	}

	public String getRejectReason() {
		return this.data.get(REJECT_REASON);
	}

	public void setRejectReason(String rejectReason) {
		this.data.put(REJECT_REASON, rejectReason);
	}

	public void setImgHash(String imgHash) {
		this.data.put(IMAGE_HASH, imgHash);
	}

	public String getImgHash(String imgHash) {
		return this.data.get(IMAGE_HASH);
	}
}
