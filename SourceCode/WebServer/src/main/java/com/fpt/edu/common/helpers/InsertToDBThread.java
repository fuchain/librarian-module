package com.fpt.edu.common.helpers;

import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.*;
import com.fpt.edu.services.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class InsertToDBThread {
	protected final Logger LOGGER = LogManager.getLogger(getClass());
	private CategoryServices categoryServices;
	private AuthorServices authorServices;
	private PublisherServices publisherServices;
	private BookDetailsServices bookDetailsServices;
	private UserServices userServices;
	private BookServices bookServices;


	public InsertToDBThread(CategoryServices categoryServices, AuthorServices authorServices, PublisherServices publisherServices, BookDetailsServices bookDetailsServices, BookServices bookServices, UserServices userServices) {
		this.categoryServices = categoryServices;
		this.authorServices = authorServices;
		this.publisherServices = publisherServices;
		this.bookDetailsServices = bookDetailsServices;
		this.userServices = userServices;
		this.bookServices = bookServices;
	}


	@Transactional
	public void importBook(JSONObject rawData) throws Exception {
		User librarian = userServices.getFirstLibrarian();
		BookDetail bookDetail = bookDetailsServices.getBookByISBN(rawData.getString(Constant.ISBN), rawData.getString("name"), rawData.getString("libol"));
		if (bookDetail.getName() == null) {
			bookDetail.setCategories(categoryServices.addIfNotExist(rawData.getString(Constant.CATEGORY)));
			JSONArray authors = rawData.getJSONArray(Constant.AUTHORS);
			bookDetail.setAuthors(new ArrayList<>());
			for (int i = 0; i < authors.length(); i++) {
				String authorName = authors.getString(i);
				Author a = authorServices.getAndAddIfNotExist(authorName);
				bookDetail.getAuthors().add(a);
			}
			Publisher p = publisherServices.getandAddIfNotExist(rawData.getString(Constant.PUBLISHER));
			bookDetail.setPublisher(p);
			bookDetail.setLibol(rawData.getString("libol"));
			bookDetail.setName(rawData.getString("name"));
			bookDetail.setIsbn(rawData.getString(Constant.ISBN));
			String subject_code = "";
			if (rawData.has(Constant.SUBJECT_CODE_KEY)) {
				subject_code = rawData.getString(Constant.SUBJECT_CODE_KEY);
			}

			bookDetail.setSubjectCode(subject_code);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String publishedDate = "2000-01-01";
			if (rawData.has("publishedDate")) {
				publishedDate = rawData.getString("publishedDate");
			}
			bookDetail.setPublishedDate((publishedDate));
			String previewLink = Constant.DEFAULT_REVIEW_LINK;
			if (rawData.has(Constant.PREVIEW_LINK)) {
				previewLink = rawData.getString(Constant.PREVIEW_LINK);
			}
			bookDetail.setPreviewLink(previewLink);
			String thumbnail = Constant.DEFAULT_IMAGE_LINK;
			if (rawData.has(Constant.IMAGE_THUMBNAIL)) {
				thumbnail = rawData.getString(Constant.IMAGE_THUMBNAIL);

			}
			bookDetail.setThumbnail(thumbnail);
			String desc = Constant.DEFAULT_DESCRIPTION;
			if (rawData.has(Constant.DESCRIPTION)) {
				desc = rawData.getString(Constant.DESCRIPTION);
			}
			bookDetail.setDescription(desc);
			if (bookDetail.getAuthors().size() > 0) {
				LOGGER.info("Authos: " + bookDetail.getAuthors().get(0).getName());
			}
			LOGGER.info("Description: " + bookDetail.getDescription());
			LOGGER.info("ISBN : " + bookDetail.getIsbn());
			LOGGER.info("Thumbnail: " + bookDetail.getThumbnail());
			LOGGER.info("publisher: " + bookDetail.getPublisher().getName());
			LOGGER.info("Subject code: " + bookDetail.getSubjectCode());
			LOGGER.info("preview Link: " + bookDetail.getPreviewLink());
			LOGGER.info("publishDate: " + bookDetail.getPublishedDate());
			bookDetailsServices.saveBookDetail(bookDetail);
			int numberOfBook = rawData.getInt("remaining");
			List<Book> bookList = new ArrayList<>();
			for (int i = 0; i < numberOfBook; i++) {
				Book book = new Book();
				book.setBookDetail(bookDetail);
				book.setUser(librarian);

				BookMetadata bookMetadata = book.getMetadata();
				bookMetadata.setStatus(book.getStatus());
				bookServices.saveBook(book);
				//bookMetadata.setTransactionTimestamp(String.valueOf(System.currentTimeMillis() / 1000L));
				bookMetadata.setTransactionTimestamp(String.valueOf(book.getCreateDate().getTime() / 1000));
				BookAsset bookAsset = book.getAsset();
				bookAsset.setBookId(String.valueOf(book.getId()));
				// for estimate save book to bighchian
				long startTime = System.currentTimeMillis();
				BigchainTransactionServices services = new BigchainTransactionServices();
				services.doCreate(
					bookAsset.getData(), bookMetadata.getData(),
					String.valueOf(book.getUser().getEmail()),
					(transaction, response) -> {
						String transactionId = transaction.getId();
						book.setAssetId(transactionId);
						book.setLastTxId(transactionId);
						bookServices.saveBook(book);
						if (!book.getAssetId().isEmpty()) {
							bookList.add(book);
						}
					}, (transaction, response) -> {
						LOGGER.error(transaction.getMetaData());
					});

				long stopTime = System.currentTimeMillis();
				LOGGER.info("Insert 1 book to bighchain take " + (stopTime - startTime));
				Thread.sleep(500);
			}
			bookDetail.setBooks(bookList);
			bookDetailsServices.saveBookDetail(bookDetail);
			LOGGER.info("Import success " + bookList.size() + " book with name " + bookDetail.getName());
		} else {

			List<Book> bookList = new ArrayList<>();
			int numberOfBook = rawData.getInt("remaining") - bookDetail.getBooks().size();
			if (numberOfBook == 0) {
				LOGGER.info("Book name " + bookDetail.getName() + " was import with " + bookDetail.getBooks().size() + " instances");

			}
			for (int i = 0; i < numberOfBook; i++) {
				Book book = new Book();
				book.setBookDetail(bookDetail);
				book.setUser(librarian);
				BookMetadata bookMetadata = book.getMetadata();
				bookMetadata.setStatus(book.getStatus());
				bookServices.saveBook(book);
				bookMetadata.setTransactionTimestamp(String.valueOf(book.getCreateDate().getTime() / 1000));
				BookAsset bookAsset = book.getAsset();
				bookAsset.setBookId(String.valueOf(book.getId()));
				long startTime = System.currentTimeMillis();
				BigchainTransactionServices services = new BigchainTransactionServices();
				services.doCreate(
					bookAsset.getData(), bookMetadata.getData(),
					String.valueOf(book.getUser().getEmail()),
					(transaction, response) -> {
						String transactionId = transaction.getId();
						book.setAssetId(transactionId);
						book.setLastTxId(transactionId);
						if (!book.getAssetId().isEmpty()) {
							bookList.add(book);
							bookDetail.setBooks(bookList);
						}
					}, (transaction, response) -> {
						LOGGER.error(transaction.getMetaData());
						LOGGER.error("Error when insert to bigchian " + response.message());
					});
				long stopTime = System.currentTimeMillis();
				LOGGER.info("Insert 1 book to bighchain take " + (stopTime - startTime));
				Thread.sleep(500);
			}

		}


	}


}
