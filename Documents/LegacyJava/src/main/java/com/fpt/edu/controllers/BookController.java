package com.fpt.edu.controllers;

import com.fpt.edu.common.helpers.ImportHelper;
import com.fpt.edu.services.NotificationService;
import com.fpt.edu.common.request_queue_simulate.PublishSubscribe;
import com.fpt.edu.common.request_queue_simulate.RequestQueueManager;
import com.fpt.edu.entities.Book;
import com.fpt.edu.exceptions.EntityIdMismatchException;
import com.fpt.edu.services.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("books")
public class BookController extends BaseController {

	public BookController(UserServices userServices, BookDetailsServices bookDetailsServices, BookServices bookServices, ImportHelper importHelper, MatchingServices matchingServices, RequestServices requestServices, PublishSubscribe publishSubscribe, RequestQueueManager requestQueueManager, NotificationService notificationService) {
		super(userServices, bookDetailsServices, bookServices, importHelper, matchingServices, requestServices, publishSubscribe, requestQueueManager, notificationService);
	}

	@ApiOperation(value = "Get a book by its id")
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return new ResponseEntity<>(bookServices.getBookById(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Create a book")
    @PostMapping("")
    public ResponseEntity<Book> insertBook(@RequestBody Book book) {
        return new ResponseEntity<>(bookServices.saveBook(book), HttpStatus.OK);
    }

    @ApiOperation(value = "Update a book")
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) throws EntityIdMismatchException {
        if (!book.getId().equals(id)) {
            throw new EntityIdMismatchException("Book ID: " + id + " and " + book.getId() + " does not match");
        }

        Book existedBook = bookServices.getBookById(id);
        Book bookResult = bookServices.updateBook(existedBook);

        return new ResponseEntity<>(bookResult, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete a book")
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        Book existedBook = bookServices.getBookById(id);
        bookServices.deleteBook(existedBook.getId());
    }

}
