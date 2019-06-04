package com.fpt.edu.controller;

import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.*;
import com.fpt.edu.repository.*;
import com.fpt.edu.services.BigchainTransactionServices;
import com.fpt.edu.services.BookDetailsServices;
import com.fpt.edu.services.UserServices;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.*;

@RestController
@RequestMapping("simulate_datas")
public class SimulateDataController extends BaseController {

    Logger logger = LoggerFactory.getLogger(SimulateDataController.class);

    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    PublisherRepository publisherRepository;
    @Autowired
    BookDetailRepository bookDetailRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    private UserServices userServices;
    @Autowired
    private BookDetailsServices bookDetailsServices;

    @RequestMapping(value = "/init", method = RequestMethod.POST, produces = Constant.APPLICATION_JSON)
    @Transactional
    public String addSimulateBookData() throws Exception {
        Iterable<Book> iterable = bookRepository.findAll();
        if (!((Collection) iterable).isEmpty()) {
            return "Book is already exist!!!";
        }

        Random random = new Random();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User librarian = userServices.getUserByEmail(email);

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
        List<BookDetail> bookDetails = new ArrayList<>();
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
            for (int i = 0; i < 10; i++) {
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
            }
        }
        return "Init simulate data completed!";
    }

    @RequestMapping(value = "/give_book", method = RequestMethod.POST, produces = Constant.APPLICATION_JSON)
    public String giveBooksToReader(@RequestBody String body) throws Exception {
        Random random = new Random();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User librarian = userServices.getUserByEmail(email);

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
                        (transaction, response) -> {}
                );
            }
            return "Give book completed";
        } else {
            return "Number of book is out of range";
        }
    }
}
