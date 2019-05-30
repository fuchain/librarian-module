package com.fpt.edu.controller;

import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.*;
import com.fpt.edu.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("simulate_datas")
public class SimulateDataController {

    @Autowired
    AuthorRepository authorRepository ;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    PublisherRepository publisherRepository;
    @Autowired
    BookDetailRepository bookDetailRepository;
    @Autowired
    BookRepository bookRepository;

    @RequestMapping(value = "/init", method = RequestMethod.POST, produces = Constant.APPLICATION_JSON)
    public String addSimulateBookData() throws Exception {
        Random random = new Random();

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
        for (String bookDetailName : bookDetailNames) {
            BookDetail bookDetail = new BookDetail();
            bookDetail.setName(bookDetailName);
            bookDetail.setBookStartDate(new Date());

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

            // Init 10 book instance
            List<Book> books = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                Book book = new Book();
                book.setBookDetail(bookDetail);
                bookRepository.save(book);
                books.add(book);
            }
            bookDetail.setBooks(books);
            bookDetailRepository.save(bookDetail);
        }
        return "Init simulate data completed!";
    }
}
