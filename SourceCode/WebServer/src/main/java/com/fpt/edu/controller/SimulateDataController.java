package com.fpt.edu.controller;

import com.fpt.edu.entities.*;
import com.fpt.edu.repository.*;
import com.fpt.edu.services.BigchainTransactionServices;
import com.fpt.edu.services.UserServices;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("simulate_datas")
public class SimulateDataController extends BaseController {
	private final AuthorRepository authorRepository;
	private final CategoryRepository categoryRepository;
	private final PublisherRepository publisherRepository;
	private final BookDetailRepository bookDetailRepository;
	private final BookRepository bookRepository;
	private final UserServices userServices;

	@Autowired
	public SimulateDataController(AuthorRepository authorRepository, CategoryRepository categoryRepository,
								  PublisherRepository publisherRepository, BookDetailRepository bookDetailRepository,
								  BookRepository bookRepository, UserServices userServices) {
		this.authorRepository = authorRepository;
		this.categoryRepository = categoryRepository;
		this.publisherRepository = publisherRepository;
		this.bookDetailRepository = bookDetailRepository;
		this.bookRepository = bookRepository;
		this.userServices = userServices;
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
		String[] categoryNames = {"Computer Scienece", "Software Engineering", "Business Management"};
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
		String[] bookDetailNames = {"XML", "C Sharp", "JAVA", "JAVASCRIPT", "SPRING", "BigchainDB", "Data Structure", "Algorithm", "Network", "Machine Learning"};
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
			bookDetail.setCategories(categoryList);

			// Map publiser to book detail
			bookDetail.setPublisher(publishers.get(random.nextInt(publishers.size())));

			bookDetailRepository.save(bookDetail);

			// Init 10 book instance
			List<Book> bookList = new ArrayList<>();
			for (int i = 0; i < 1; i++) {
				Book book = new Book();
				book.setId(Long.valueOf(count));
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
				book.setUser(receiver);
				services.doTransfer(
					book.getLastTxId(),
					book.getAssetId(), book.getMetadata(),
					librarian.getEmail(), receiver.getEmail(),
					(transaction, response) -> {
						book.setLastTxId(transaction.getId());
						bookRepository.save(book);
					},
					(transaction, response) -> {
					}
				);
				Thread.sleep(500);
			}
			return "Give book completed";
		} else {
			return "Number of book is out of range";
		}
	}
}
