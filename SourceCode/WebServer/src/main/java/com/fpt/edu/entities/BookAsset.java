package com.fpt.edu.entities;

import java.util.Map;
import java.util.TreeMap;

import static com.fpt.edu.constant.Constant.BOOK_ID;
import static com.fpt.edu.constant.Constant.EMPTY_VALUE;

public class BookAsset {

	private Map<String, String> data;

	public BookAsset() {
		this.data = new TreeMap<>();
		this.data.put(BOOK_ID, EMPTY_VALUE);
	}

	public BookAsset(String bookId) {
		this.data = new TreeMap<>();
		this.data.put(BOOK_ID, bookId);
	}

	public void setBookId(String bookId) {
		this.data.put(BOOK_ID, bookId);
	}

	public Map<String, String> getData() {
		return this.data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}
}
