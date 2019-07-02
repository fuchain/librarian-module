package com.fpt.edu.controllers;

import com.fpt.edu.common.helpers.ImportHelper;
import com.fpt.edu.services.NotificationService;
import com.fpt.edu.common.request_queue_simulate.PublishSubscribe;
import com.fpt.edu.common.request_queue_simulate.RequestQueueManager;
import com.fpt.edu.entities.*;
import com.fpt.edu.repositories.*;
import com.fpt.edu.services.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.security.Principal;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("simulate_datas")
public class SimulateDataController extends BaseController {
	@Autowired
	BookRepository bookRepository;
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	AuthorRepository authorRepository;
	@Autowired
	PublisherRepository publisherRepository;
	@Autowired
	BookDetailRepository bookDetailRepository;
	@Autowired
	BigchainTransactionServices bigchainTransactionServices;

	public SimulateDataController(
		UserServices userServices, BookDetailsServices bookDetailsServices, BookServices bookServices,
		ImportHelper importHelper, MatchingServices matchingServices, RequestServices requestServices,
		PublishSubscribe publishSubscribe, RequestQueueManager requestQueueManager,
		BigchainTransactionServices bigchainTransactionServices, NotificationService notificationService
	) {
		super(
			userServices, bookDetailsServices, bookServices, importHelper, matchingServices,
			requestServices, publishSubscribe, requestQueueManager, notificationService
		);
		this.bigchainTransactionServices = bigchainTransactionServices;
	}

	@PostMapping("/init")
	@Transactional
	public String addSimulateBookData(Principal principal) throws Exception {
		Iterable<Book> iterable = bookRepository.findAll();
		if (!((Collection) iterable).isEmpty()) {
			return "Book is already exist!!!";
		}

		Random random = new Random();
		User librarian = userServices.getUserByEmail(principal.getName());

		// Init data for  category
		List<Category> categories = new ArrayList<>();
		String[] categoryNames = {"Computer Science", "Software Engineering", "Business Management"};
		for (String categoryName : categoryNames) {
			Category category = new Category();
			category.setName(categoryName);
			categories.add(category);
			categoryRepository.save(category);
		}

		// Init data for author
		List<Author> authors = new ArrayList<>();
		String[] authorNames = {"Adam Licol", "John Thanos", "Alex Man"};
		for (String authorName : authorNames) {
			Author author = new Author();
			author.setName(authorName);
			authors.add(author);
			authorRepository.save(author);
		}

		// Init data for publisher
		List<Publisher> publishers = new ArrayList<>();
		String[] publisherNames = {"Springer Nature", "Scholastic", "McGraw-Hill Education", "HarperCollins", "Cengage"};
		for (String publisherName : publisherNames) {
			Publisher publisher = new Publisher();
			publisher.setName(publisherName);
			publishers.add(publisher);
			publisherRepository.save(publisher);
		}

		// Init data for book detail
		String[] bookDetailNames = {
			"Introduction to Databases",
			"Discrete Mathematics",
			"Object-Oriented Programming",
			"Front-end Web Development",
			"Data Structures and Algorithms",
			"Operating Systems",
			"Introduction to Software Engineering",
			"Web-based Java Applications",
			"Computer Networking",
			"Software Quality Assurance and Testing",
			"Software Requirements",
			".NET and C#"
		};
		int count = 1;
		for (String bookDetailName : bookDetailNames) {
			BookDetail bookDetail = new BookDetail();
			bookDetail.setName(bookDetailName);

			// Map author to book detail
			List<Author> authorList = new ArrayList<>();
			authorList.add(authors.get(random.nextInt(authors.size())));
			bookDetail.setAuthors(authorList);

			// Map category to book detail
			List<Category> categoryList = new ArrayList<>();
			categoryList.add(categories.get(random.nextInt(categories.size())));
			bookDetail.setCategories(categoryList.get(0));

			// Map publisher to book detail
			bookDetail.setPublisher(publishers.get(random.nextInt(publishers.size())));

			bookDetailRepository.save(bookDetail);

			// Init 10 book instance
			List<Book> bookList = new ArrayList<>();
			for (int i = 0; i < 1; i++) {
				Book book = new Book();
				book.setId((long) count);
				book.setBookDetail(bookDetail);
				book.setUser(librarian);

				BookMetadata bookMetadata = book.getMetadata();
				bookMetadata.setStatus(book.getStatus());
				bookMetadata.setTransactionTimestamp(String.valueOf(System.currentTimeMillis() / 1000L));

				BookAsset bookAsset = book.getAsset();
				bookAsset.setBookId(String.valueOf(book.getId()));

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
					});
				count++;
				Thread.sleep(500);
			}
		}

		return "Init simulate data completed!";
	}

	@PostMapping("/give_book")
	public String giveBooksToReader(@RequestBody String body, Principal principal) throws Exception {
		Random random = new Random();

		// Get user information
		User librarian = userServices.getUserByEmail(principal.getName());

		//Get email and book_number from Request Body
		JSONObject jsonBody = new JSONObject(body);
		User receiver = userServices.getUserByEmail(jsonBody.getString("email"));
		int bookNumber = Integer.parseInt(jsonBody.getString("book_number"));

		BigchainTransactionServices services = new BigchainTransactionServices();
		List<Book> bookList = (List<Book>) bookRepository.findBookListByUserId(librarian.getId());

		if (bookList.size() >= bookNumber) {
			for (int i = 0; i < bookNumber; i++) {
				Book book = bookList.get(random.nextInt(bookList.size()));

				bookServices.getLastTransactionFromBigchain(book);
				BookMetadata bookMetadata = book.getMetadata();
				bookMetadata.setTransactionTimestamp(String.valueOf(System.currentTimeMillis() / 1000L));

				book.setUser(receiver);
				services.doTransfer(
					book.getLastTxId(),
					book.getAssetId(), book.getMetadata().getData(),
					librarian.getEmail(), receiver.getEmail(),
					(transaction, response) -> {
						book.setLastTxId(transaction.getId());
						bookRepository.save(book);
					},
					(transaction, response) -> {
					}
				);
				Thread.sleep(100);
			}
			return "Give book completed";
		} else {
			return "Number of book is out of range";
		}
	}

	@PostMapping("/restore_bigchain_from_db")
	public String restoreBigchainFromDB() throws Exception {
		int pageIndex = 0;
		int pageSize = 1000;
		Pageable pageable;
		pageable = PageRequest.of(pageIndex, pageSize);
		while (true) {
			Page<Book> bookPage = bookRepository.findAll(pageable);
			insertBookBackToBC(bookPage.getContent());
			if (bookPage.hasNext()) {
				pageable = bookPage.nextPageable();
			} else {
				break;
			}
		}
		return "Inserting to bigchain.....";
	}

	private void insertBookBackToBC(List<Book> bookList) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		for (Book book : bookList) {
			String currentKeeper = book.getUser().getEmail();
			BookMetadata bookMetadata = new BookMetadata(currentKeeper, book.getStatus());
			book.setMetadata(bookMetadata);
			bookMetadata.setTransactionTimestamp(String.valueOf(
				dateFormat.parse(book.getCreateDate().toString()).getTime() / 1000L)
			);
			bigchainTransactionServices.doCreate(
				book.getAsset().getData(),
				bookMetadata.getData(),
				currentKeeper,
				(transaction, response) -> {
					String bcAssetId = transaction.getId();
					String assetId = book.getAssetId();
					if (bcAssetId.equals(assetId)) {
						LOGGER.info("Insert asset id " + bcAssetId + " correctly!!!");
					} else {
						LOGGER.error("Insert asset id " + bcAssetId + " failed!!!");
						LOGGER.error("Book asset id: " + assetId);
					}
				},
				(transaction, response) -> {
				}
			);
		}
	}

	@PostMapping("/create_draft_tx")
	public String createDrarfTx(Principal principal) throws Exception {
		User user = userServices.getUserByEmail(principal.getName());
		Map asset = new TreeMap();
		SecureRandom random = new SecureRandom();
		asset.put("abc", random.nextInt());
		asset.put("xyz", random.nextInt());
		asset.put("tx_ts", System.currentTimeMillis() / 1000L);
		Map metadata = new TreeMap();
		metadata.put("abc", random.nextInt());
		metadata.put("xyz", random.nextInt());
		metadata.put("tx_ts", System.currentTimeMillis() / 1000L);
		bigchainTransactionServices.doCreate(asset, metadata, user.getEmail());

		return "Submited to bigchain!!!";
	}
}
