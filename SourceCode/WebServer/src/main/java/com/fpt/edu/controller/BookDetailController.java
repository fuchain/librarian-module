package com.fpt.edu.controller;

import com.fpt.edu.entities.BookDetail;
import com.fpt.edu.exception.EntityIdMismatchException;
import com.fpt.edu.services.BookDetailsServices;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping("bookdetails")
public class BookDetailController extends BaseController {
    private final BookDetailsServices bookDetailsServices;

    @Autowired
    public BookDetailController(BookDetailsServices bookDetailsServices) {
        this.bookDetailsServices = bookDetailsServices;
    }

    @ApiOperation(value = "Create a book detail ", response = BookDetail.class)
    @PostMapping("")
    public ResponseEntity<BookDetail> createBookDetail(@RequestBody BookDetail bookDetail) {
        return new ResponseEntity<>(bookDetailsServices.saveBookDetail(bookDetail), HttpStatus.OK);
    }

    @ApiOperation(value = "Get a book detail by its id", response = BookDetail.class)
    @GetMapping("/{id}")
    public ResponseEntity<BookDetail> getBookDetail(@PathVariable Long id) {
        BookDetail bookDetail = bookDetailsServices.getBookById(id);

        return new ResponseEntity<>(bookDetail, HttpStatus.OK);
    }

    @ApiOperation(value = "Update a book detail", response = BookDetail.class)
    @PutMapping("/{id}")
    public ResponseEntity<BookDetail> updateBookDetail(@PathVariable Long id, @RequestBody BookDetail bookDetail)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, EntityIdMismatchException {
        if (!bookDetail.getId().equals(id)) {
            throw new EntityIdMismatchException("Book id: " + id + " does not match " + bookDetail.getId());
        }

        bookDetailsServices.getBookById(bookDetail.getId());

        BookDetail updateBookDetail = bookDetailsServices.updateBookDetail(bookDetail);
        return new ResponseEntity<>(updateBookDetail, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete a book detail")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBookDetail(@PathVariable Long id) {
        bookDetailsServices.deleteBookDetail(id);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @ApiOperation(value = "Search for a book detail", response = BookDetail.class)
    @GetMapping("search")
    public ResponseEntity<List<BookDetail>> searchBook(@RequestParam("name") String name) {
        List<BookDetail> books = bookDetailsServices.searchBookDetails(name.toLowerCase());
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
}
