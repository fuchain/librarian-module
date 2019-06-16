package com.fpt.edu.common.helpers;

import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.Author;
import com.fpt.edu.entities.BookDetail;
import com.fpt.edu.services.AuthorServices;
import com.fpt.edu.services.CategoryServices;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;

@Component
public class ImportHelper {
	protected final Logger LOGGER = LogManager.getLogger(getClass());

	private JSONArray rawData;
	private boolean isEndOfInputData;
	private Queue<JSONObject> queue;

	private CategoryServices categoryServices;
	private AuthorServices authorServices;
	@Autowired
	public ImportHelper(CategoryServices categoryServices, AuthorServices authorServices) {
		this.categoryServices = categoryServices;
		this.authorServices=authorServices;
	}

	public void initData(JSONArray rawData) {
		this.rawData = rawData;
		this.isEndOfInputData = false;
		this.queue = new LinkedList<>();

	}


	public boolean startImport() {
		Thread getDataThread = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < rawData.length(); i++) {
					queue.add(rawData.getJSONObject(i));
				}
				isEndOfInputData = true;
			}
		});
		Thread insertDB = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!queue.isEmpty() || !isEndOfInputData) {
					if (!queue.isEmpty()) {
						JSONObject current = queue.poll();
						if (current != null) {
							LOGGER.info("Processing the book Details");
							LOGGER.info(current.toString());
							// get book detail info from google APIS
							String isbn = current.getString(Constant.ISBN);
							if (isbn.isEmpty()) {
								importBookWihNoExtraInfo(current);
							} else {
								try {
									JSONObject rawData = getBookDetailByISBN(isbn);
									if (rawData.getInt("totalItems") != 0) {
										JSONObject jsonData = rawData.getJSONObject(Constant.ITEMS);
										current.put(Constant.PREVIEW_LINK, jsonData.getString(Constant.PREVIEW_LINK));
										current.put(Constant.DESCRIPTION, jsonData.getString(Constant.DESCRIPTION));
										if (jsonData.has("imageLinks")) {
											current.put(Constant.IMAGE_THUMBNAIL, jsonData.getJSONObject("imageLinks").getString(Constant.IMAGE_THUMBNAIL));
										}
										current.put(Constant.PUBLISHER, jsonData.getString(Constant.PUBLISHER));
										current.put(Constant.PUBLISHED_DATE, jsonData.getString(Constant.PUBLISHED_DATE));
										current.put(Constant.AUTHORS, jsonData.getJSONArray(Constant.AUTHORS));


									} else {
										importBookWihNoExtraInfo(current);
									}
								} catch (UnirestException e) {
									LOGGER.error("Error when get book with ISBN " + isbn);
								}
							}
						}
					} else {
						LOGGER.info("Queue is empty");
					}
				}
				LOGGER.info("End processing");
			}
		});
		getDataThread.start();
		insertDB.start();
		return false;
	}


	public JSONObject getBookDetailByISBN(String isbn) throws UnirestException {
		return Unirest.get(Constant.GOOGLE_BOOK_API).header("accept", "application/json")
			.header("Content-Type", "application/json").queryString("q", "isbn:" + isbn).asJson().getBody().getObject();
	}


	public void importBookWihNoExtraInfo(JSONObject rawData) {
		BookDetail bookDetail = new BookDetail();
		bookDetail.setCategories(categoryServices.addIfNotExist(rawData.getString(Constant.CATEGORY)));


	}

	public void importBookWithExtraInfo() {


	}


}
