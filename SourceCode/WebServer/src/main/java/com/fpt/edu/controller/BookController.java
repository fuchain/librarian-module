package com.fpt.edu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.Book;
import com.fpt.edu.exception.EntityIdMismatchException;
import com.fpt.edu.exception.EntityNotFoundException;
import com.fpt.edu.services.BookServices;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("books")
public class BookController extends BaseController {

    @Autowired
    private BookServices bookServices;

    @ApiOperation(value = "Get a book by its id", response = String.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> getBookById(@PathVariable Long id) throws JsonProcessingException, EntityNotFoundException {
        Book book = bookServices.getBookById(id);
        if (book == null) {
            throw new EntityNotFoundException("Book id: " + id + " not found");
        }

        JSONObject jsonObject = utils.convertObjectToJSONObject(book);
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @ApiOperation(value = "Create a book", response = String.class)
    @RequestMapping(value = "", method = RequestMethod.POST, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> insertBook(@RequestBody String body) throws IOException {
        Book bookResult = bookServices.saveBook(body);
        JSONObject jsonObject = utils.convertObjectToJSONObject(bookResult);
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @ApiOperation(value = "Update a book", response = String.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> updateBook(@PathVariable Long id, @RequestBody Book book) throws IOException, EntityNotFoundException, EntityIdMismatchException {
        Long bookId = book.getId();

        if (bookId != id) {
            throw new EntityIdMismatchException("Book ID: " + id + " and " + bookId + " does not match");
        }

        Book existedBook = bookServices.getBookById(id);
        if (existedBook == null) {
            throw new EntityNotFoundException("Book id: " + id + " not found");
        }

        Book bookResult = bookServices.updateBook(existedBook);

        JSONObject jsonObject = utils.convertObjectToJSONObject(bookResult);
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @ApiOperation(value = "Delete a book", response = String.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> deleteBook(@PathVariable Long id) throws EntityNotFoundException {
        Book existedBook = bookServices.getBookById(id);
        if (existedBook == null) {
            throw new EntityNotFoundException("Book id: " + id + " not found");
        }

        bookServices.deleteBook(id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "success");

        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

}
