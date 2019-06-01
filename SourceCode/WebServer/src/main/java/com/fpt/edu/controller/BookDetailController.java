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
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("bookdetails")
public class BookDetailController extends BaseController {
    @Autowired
    BookDetailsServices bookDetailsServices;

    @ApiOperation(value = "Get a list of book details", response = String.class)
    @RequestMapping(value = "", method = RequestMethod.GET, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> findBookDetailsById() throws EntityNotFoundException, JsonProcessingException {
        String requestPattern = httpServletRequest.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();
        LOGGER.info("START Controller :" + requestPattern);
        JSONObject raw = utils.buildListEntity(bookDetailsServices.getAllBookDetails(), httpServletRequest);
        return new ResponseEntity<>(raw.toString(), HttpStatus.OK);
    }
    @ApiOperation(value = "Create a bookdetails ", response = String.class)
    @RequestMapping(value = "", method = RequestMethod.POST, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> createBookDetails(@RequestBody String body) throws IOException {
        BookDetail detail = bookDetailsServices.saveBookDetail(body);
        return new ResponseEntity<>(utils.convertObjectToJSONObject(detail).toString(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get a bookdetails instance", response = String.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> getBookDetail(@PathVariable Long id) throws IOException,EntityNotFoundException {
        BookDetail bookDetail = bookDetailsServices.getBookById(id);
        return new ResponseEntity<>(utils.convertObjectToJSONObject(bookDetail).toString(), HttpStatus.OK);
    }
    @ApiOperation(value = "Update a bookdetails", response = String.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> updateBookDetail(@PathVariable Long id, @RequestBody String body) throws IOException {
        BookDetail bookDetail = bookDetailsServices.updateBookDetail(body);
        return new ResponseEntity<>(utils.convertObjectToJSONObject(bookDetail).toString(), HttpStatus.OK);
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
        List<BookDetail> books = bookDetailsServices.searchBookDetails(name);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
}
