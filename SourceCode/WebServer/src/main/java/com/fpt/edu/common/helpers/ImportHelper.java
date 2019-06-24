package com.fpt.edu.common.helpers;

import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.*;
import com.fpt.edu.services.*;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Service
@Transactional
public class ImportHelper {
	protected final Logger LOGGER = LogManager.getLogger(getClass());

	private JSONArray rawData;
	private boolean isEndOfInputData;
	private Queue<JSONObject> queue;

	private CategoryServices categoryServices;
	private AuthorServices authorServices;
	private PublisherServices publisherServices;
	private BookDetailsServices bookDetailsServices;
	private UserServices userServices;
	private BookServices bookServices;

	@Autowired
	public ImportHelper(CategoryServices categoryServices, AuthorServices authorServices,PublisherServices publisherServices, BookDetailsServices bookDetailsServices,BookServices bookServices,UserServices userServices) {
		this.categoryServices = categoryServices;
		this.authorServices = authorServices;
		this.publisherServices=publisherServices;
		this.bookDetailsServices=bookDetailsServices;
		this.userServices=userServices;
		this.bookServices=bookServices;
	}
	public void initData(JSONArray rawData) {
		this.rawData = rawData;
		this.isEndOfInputData = false;
		this.queue = new LinkedList<>();
	}
	@Transactional
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
								setDefaultData(current);
								try {
									importBook(current);
								} catch (ParseException e) {
									LOGGER.error(e.getMessage());
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								try {
									JSONObject rawData = getBookDetailByISBN(isbn);
									if (rawData.getInt("totalItems") != 0) {
										JSONObject jsonData = rawData.getJSONArray(Constant.ITEMS).getJSONObject(0).getJSONObject("volumeInfo");
										current.put(Constant.PREVIEW_LINK, jsonData.getString(Constant.PREVIEW_LINK));
										current.put(Constant.DESCRIPTION, jsonData.getString(Constant.DESCRIPTION));
										if (jsonData.has("imageLinks")) {
											current.put(Constant.IMAGE_THUMBNAIL, jsonData.getJSONObject("imageLinks").getString(Constant.IMAGE_THUMBNAIL));
										}else{
											current.put(Constant.IMAGE_THUMBNAIL, Constant.DEFAULT_IMAGE_LINK);
										}
										current.put(Constant.PUBLISHER, jsonData.getString(Constant.PUBLISHER));
										current.put(Constant.PUBLISHED_DATE, jsonData.getString(Constant.PUBLISHED_DATE));
										current.put(Constant.AUTHORS, jsonData.getJSONArray(Constant.AUTHORS));
										importBook(current);
									} else {
										setDefaultData(current);
										importBook(current);
									}
									LOGGER.info(current.toString());
								} catch (UnirestException | ParseException e) {
									LOGGER.error("Error when get book with ISBN " + isbn);
								} catch (Exception e) {
									e.printStackTrace();
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


	public void setDefaultData(JSONObject rawData){
		JSONObject jsonData = rawData.getJSONObject(Constant.ITEMS);
		rawData.put(Constant.PREVIEW_LINK, Constant.DEFAULT_REVIEW_LINK);
		rawData.put(Constant.DESCRIPTION, Constant.DEFAULT_DESCRIPTION);
		rawData.put(Constant.IMAGE_THUMBNAIL,Constant.DEFAULT_IMAGE_LINK);
		rawData.put(Constant.PUBLISHER, Constant.DEFAULT_PUBLISHER);
		rawData.put(Constant.PUBLISHED_DATE, Constant.DEFAULT_PUBLISH_DATE);
		JSONArray arr = new JSONArray();
		arr.put( Constant.DEFAULT_AUTHOR);
		rawData.put(Constant.AUTHORS, arr);

	}


	public void importBook(JSONObject rawData) throws Exception {
		BookDetail bookDetail = new BookDetail();
		bookDetail.setCategories(categoryServices.addIfNotExist(rawData.getString(Constant.CATEGORY)));
		JSONArray authors = rawData.getJSONArray(Constant.AUTHORS);
		bookDetail.setAuthors(new ArrayList<>());
		for (int i = 0; i <authors.length() ; i++) {
			String authorName=authors.getString(i);
			Author a = authorServices.getAndAddIfNotExist(authorName);
			bookDetail.getAuthors().add(a);
		}
		Publisher p = publisherServices.getandAddIfNotExist(rawData.getString(Constant.PUBLISHER));
		bookDetail.setPublisher(p);
		bookDetail.setLibol(rawData.getString("libol"));
		bookDetail.setName(rawData.getString("name"));
		bookDetail.setIsbn(rawData.getString(Constant.ISBN));
		bookDetail.setSubjectCode(rawData.getString(Constant.SUBJECT_CODE_KEY));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		bookDetail.setPublishedDate(sdf.parse(rawData.getString("publishedDate")));
		bookDetail.setPreviewLink(rawData.getString(Constant.PREVIEW_LINK));
		bookDetail.setThumbnail(rawData.getString(Constant.IMAGE_THUMBNAIL));
		LOGGER.info("Authos: "+bookDetail.getAuthors().get(0).getName());
		LOGGER.info("Description: "+bookDetail.getDescription());
		LOGGER.info("ISBN : "+bookDetail.getIsbn());
		LOGGER.info("Thumbnail: "+bookDetail.getThumbnail());
		LOGGER.info("publisher: "+bookDetail.getPublisher().getName());
		LOGGER.info("Subject code: "+bookDetail.getSubjectCode());
		LOGGER.info("preview Link: "+bookDetail.getPreviewLink());
		LOGGER.info("publishDate: "+bookDetail.getPublishedDate());
		bookDetailsServices.saveBookDetail(bookDetail);
		User librarian = userServices.getFirstLibrarian();
		int numberOfBook = rawData.getInt("remaining");
		List<Book> bookList = new ArrayList<>();
		for (int i = 0; i <numberOfBook ; i++) {
			Book book = new Book();
			book.setBookDetail(bookDetail);
			book.setUser(librarian);
			BigchainTransactionServices services = new BigchainTransactionServices();
			services.doCreate(
				book.getAsset(), book.getMetadata(),
				String.valueOf(book.getUser().getEmail()),
				(transaction, response) -> {
					String trasactionId = transaction.getId();
					book.setAssetId(trasactionId);
					book.setLastTxId(trasactionId);
					if (!book.getAssetId().isEmpty()) {
						bookList.add(book);
						bookDetail.setBooks(bookList);
					}
				}, (transaction, response) -> {
					String trasactionId = transaction.getId();
					book.setAssetId(trasactionId);
					book.setLastTxId(trasactionId);
					if (!book.getAssetId().isEmpty()) {
						bookList.add(book);
						bookDetail.setBooks(bookList);
					}
				});
		}
		bookDetail.setBooks(bookList);
		bookDetailsServices.saveBookDetail(bookDetail);
		LOGGER.info("Import success "+bookList.size()+" book with name" + bookDetail.getName() );

	}






	public void importBookWithExtraInfo() {


	}


}
