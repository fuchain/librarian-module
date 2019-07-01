package com.fpt.edu.common.helpers;

import com.fpt.edu.constant.Constant;
import com.fpt.edu.services.*;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.Queue;

@Service
@Transactional
public class ImportHelper {


	protected final Logger LOGGER = LogManager.getLogger(getClass());
	int countSuccess = 0;
	Object LOCK;
	private JSONArray rawData;
	private Queue<JSONObject> queue;

	private CategoryServices categoryServices;
	private AuthorServices authorServices;
	private PublisherServices publisherServices;
	private BookDetailsServices bookDetailsServices;
	private UserServices userServices;
	private BookServices bookServices;

	@Autowired
	public ImportHelper(CategoryServices categoryServices, AuthorServices authorServices, PublisherServices publisherServices, BookDetailsServices bookDetailsServices, BookServices bookServices, UserServices userServices) {
		this.categoryServices = categoryServices;
		this.authorServices = authorServices;
		this.publisherServices = publisherServices;
		this.bookDetailsServices = bookDetailsServices;
		this.userServices = userServices;
		this.bookServices = bookServices;
		LOCK = new Object();
	}

	public void initData(JSONArray rawData) {
		this.rawData = rawData;
		this.queue = new LinkedList<>();
	}

	public boolean startImport() {

		for (int i = 0; i < rawData.length(); i++) {
			queue.add(rawData.getJSONObject(i));
		}


		Thread insertDB = new Thread(new Runnable() {
			@Override
			public void run() {
				insertDataRunable();
				LOGGER.info("End processing");
			}
		});
		Thread insertDB2 = new Thread(new Runnable() {
			@Override
			public void run() {
				insertDataRunable();
				LOGGER.info("End processing");
			}
		});
		Thread insertDB3 = new Thread(new Runnable() {
			@Override
			public void run() {
				insertDataRunable();
				LOGGER.info("End processing");
			}
		});
		Thread insertDB4 = new Thread(new Runnable() {
			@Override
			public void run() {
				insertDataRunable();
				LOGGER.info("End processing");
			}
		});
		insertDB.start();
//		insertDB2.start();
//		insertDB3.start();
//		insertDB4.start();
		return false;
	}

	private void insertDataRunable() {
		int countSuccess=0;
		synchronized (LOCK) {
			long startTime = System.currentTimeMillis();
			while (!queue.isEmpty()) {
				if (!queue.isEmpty()) {
					JSONObject current;
					synchronized (queue) {
						current = new JSONObject(queue.poll().toString());
					}
					if (current != null) {
						LOGGER.info("Processing the book Details");
						LOGGER.info(current.toString());
						// get book detail info from google APIS
						String isbn = current.getString(Constant.ISBN);
						if (isbn.isEmpty()) {
							setDefaultData(current);
							try {
								InsertToDBThread thread = new InsertToDBThread(this.categoryServices, this.authorServices, this.publisherServices, this.bookDetailsServices, this.bookServices, this.userServices);
								thread.importBook(current);
								LOGGER.info("Import success"+ (countSuccess++)+" book details" );
								LOGGER.info("Remaining bookdetails "+queue.size() );

							} catch (ParseException e) {
								LOGGER.error(e.getMessage());
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							try {
								JSONObject rawData = getBookDetailByISBN(isbn);
								if (rawData.has("totalItems") && rawData.getInt("totalItems") != 0) {
									JSONObject jsonData = rawData.getJSONArray(Constant.ITEMS).getJSONObject(0).getJSONObject("volumeInfo");
									if (jsonData.has(Constant.PREVIEW_LINK)) {
										current.put(Constant.PREVIEW_LINK, jsonData.getString(Constant.PREVIEW_LINK));
									} else {
										current.put(Constant.PREVIEW_LINK, Constant.DEFAULT_REVIEW_LINK);
									}
									if (jsonData.has(Constant.DESCRIPTION)) {
										current.put(Constant.DESCRIPTION, jsonData.getString(Constant.DESCRIPTION));
									} else {
										current.put(Constant.DESCRIPTION, Constant.DEFAULT_DESCRIPTION);
									}
									if (jsonData.has("imageLinks")) {
										current.put(Constant.IMAGE_THUMBNAIL, jsonData.getJSONObject("imageLinks").getString(Constant.IMAGE_THUMBNAIL));
									} else {
										current.put(Constant.IMAGE_THUMBNAIL, Constant.DEFAULT_IMAGE_LINK);
									}
									if (jsonData.has(Constant.PUBLISHER)) {
										current.put(Constant.PUBLISHER, jsonData.getString(Constant.PUBLISHER));
									} else {
										current.put(Constant.PUBLISHER, Constant.DEFAULT_PUBLISHER);
									}
									if (jsonData.has(Constant.PUBLISHED_DATE)) {
										current.put(Constant.PUBLISHED_DATE, jsonData.getString(Constant.PUBLISHED_DATE));
									} else {
										current.put(Constant.PUBLISHED_DATE, "");
									}
									if (jsonData.has(Constant.AUTHORS)) {
										current.put(Constant.AUTHORS, jsonData.getJSONArray(Constant.AUTHORS));
									} else {
										JSONArray arr = new JSONArray();
										current.put(Constant.AUTHORS, arr.put(Constant.DEFAULT_AUTHOR));
									}
									InsertToDBThread thread = new InsertToDBThread(this.categoryServices, this.authorServices, this.publisherServices, this.bookDetailsServices, this.bookServices, this.userServices);
									thread.importBook(current);
								} else {
									setDefaultData(current);
									InsertToDBThread thread = new InsertToDBThread(this.categoryServices, this.authorServices, this.publisherServices, this.bookDetailsServices, this.bookServices, this.userServices);
									thread.importBook(current);
								}
								LOGGER.info(current.toString());
							} catch (UnirestException | ParseException e) {
								LOGGER.error("Error when get book with ISBN " + isbn);
							} catch (Exception e) {
								e.printStackTrace();
								LOGGER.error(e.getMessage());
							}
						}
					}
				} else {

					LOGGER.info("Book import Queue is empty");
				}



			}
			LOGGER.info("Done import data from file after" + (System.currentTimeMillis() - startTime));
		}
		LOGGER.info("Import book is done with " + countSuccess + "book detail imported");
	}


	public JSONObject getBookDetailByISBN(String isbn) throws UnirestException {
		return Unirest.get(Constant.GOOGLE_BOOK_API).header("accept", "application/json")
			.header("Content-Type", "application/json").queryString("q", isbn).asJson().getBody().getObject();
	}


	public void setDefaultData(JSONObject rawData) {
		rawData.put(Constant.PREVIEW_LINK, Constant.DEFAULT_REVIEW_LINK);
		rawData.put(Constant.DESCRIPTION, Constant.DEFAULT_DESCRIPTION);
		rawData.put(Constant.IMAGE_THUMBNAIL, Constant.DEFAULT_IMAGE_LINK);
		rawData.put(Constant.PUBLISHER, Constant.DEFAULT_PUBLISHER);
		rawData.put(Constant.PUBLISHED_DATE, Constant.DEFAULT_PUBLISH_DATE);
		JSONArray arr = new JSONArray();
		arr.put(Constant.DEFAULT_AUTHOR);
		rawData.put(Constant.AUTHORS, arr);

	}


}
