package com.fpt.edu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.BookDetail;
import com.fpt.edu.exception.EntityNotFoundException;
import com.fpt.edu.services.BookDetailsServices;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping("bookdetails")
public class BookDetailController extends BaseController {
    @Autowired
    BookDetailsServices bookDetailsServices;
    @ApiOperation(value = "Get a list of book details", response = String.class)
    @RequestMapping(value = "", method = RequestMethod.GET, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<List<BookDetail>> findBookDetailsById() throws EntityNotFoundException, JsonProcessingException {
        return new ResponseEntity<>(bookDetailsServices.getAllBookDetails(), HttpStatus.OK);
    }
    @ApiOperation(value = "Create a bookdetails ", response = String.class)
    @RequestMapping(value = "", method = RequestMethod.POST, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<BookDetail> createBookDetails(@RequestBody BookDetail body) throws IOException {
        BookDetail detail = bookDetailsServices.saveBookDetail(body);
        return new ResponseEntity<>(detail, HttpStatus.OK);
    }
    @ApiOperation(value = "Get a bookdetails instance", response = String.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> getBookDetail(@PathVariable Long id) throws IOException,EntityNotFoundException {
        BookDetail bookDetail = bookDetailsServices.getBookById(id);
        return new ResponseEntity<>(utils.convertObjectToJSONObject(bookDetail).toString(), HttpStatus.OK);
    }
    @ApiOperation(value = "Update a bookdetails", response = String.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<BookDetail> updateBookDetail(@PathVariable Long id, @RequestBody BookDetail body) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, EntityNotFoundException {
        body.setId(id);
        BookDetail bookDetail = bookDetailsServices.updateBookDetail(body);
        return new ResponseEntity<>(bookDetail, HttpStatus.OK);
    }
    @ApiOperation(value = "Delete a book details", response = String.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> deleteBookDetail(@PathVariable Long id) throws IOException {
        bookDetailsServices.deleteBookDetail(id);
        JSONObject json = new JSONObject();
        json.put("Message", "Success");
        return new ResponseEntity<>(json.toString(), HttpStatus.OK);
    }
    @ApiOperation(value = "Search for a book details", response = String.class)
    @RequestMapping(value = "search", method = RequestMethod.GET, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<List<BookDetail>> searchBook(@RequestParam("name") String name) {
        List<BookDetail> books = bookDetailsServices.searchBookDetails(name.toLowerCase());
        return new ResponseEntity<>(books, HttpStatus.OK);
    }




}
