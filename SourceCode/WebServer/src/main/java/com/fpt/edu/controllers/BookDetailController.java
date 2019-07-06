package com.fpt.edu.controllers;

import com.fpt.edu.common.helpers.ImportHelper;
import com.fpt.edu.exceptions.NotFoundException;
import com.fpt.edu.services.NotificationService;
import com.fpt.edu.common.request_queue_simulate.PublishSubscribe;
import com.fpt.edu.common.request_queue_simulate.RequestQueueManager;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.BookDetail;
import com.fpt.edu.exceptions.EntityIdMismatchException;
import com.fpt.edu.services.*;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping("bookdetails")
public class BookDetailController extends BaseController {
	public BookDetailController(UserServices userServices, BookDetailsServices bookDetailsServices, BookServices bookServices, ImportHelper importHelper, MatchingServices matchingServices, RequestServices requestServices, PublishSubscribe publishSubscribe, RequestQueueManager requestQueueManager, NotificationService notificationService) {
		super(userServices, bookDetailsServices, bookServices, importHelper, matchingServices, requestServices, publishSubscribe, requestQueueManager, notificationService);
	}

	@ApiOperation(value = "Get a list of book details", response = String.class)
	@RequestMapping(value = "", method = RequestMethod.GET, produces = Constant.APPLICATION_JSON)
	public ResponseEntity<List<BookDetail>> findBookDetailsById(
		@RequestParam(name = "page", required = false, defaultValue = Constant.DEFAULT_PAGE + "") int page,
		@RequestParam(name = "size", required = false, defaultValue = Constant.DEFAULT_OFFSET + "") int size
	) {
		Pageable pageable = PageRequest.of(page - 1, size);
		return new ResponseEntity<>(bookDetailsServices.getAllBookDetails(pageable), HttpStatus.OK);
	}

	@ApiOperation(value = "Create a book detail ", response = BookDetail.class)
	@PostMapping("")
	public ResponseEntity<BookDetail> createBookDetail(@RequestBody BookDetail bookDetail) {
		return new ResponseEntity<>(bookDetailsServices.saveBookDetail(bookDetail), HttpStatus.OK);
	}

	@ApiOperation(value = "Get a book detail by its id", response = String.class)
	@GetMapping(value = "/{id}", produces = Constant.APPLICATION_JSON)
	public ResponseEntity<BookDetail> getBookDetail(@PathVariable Long id) throws NotFoundException {
		JSONObject result;
		BookDetail bookDetail = bookDetailsServices.getBookById(id);
		if (bookDetail == null) {
			throw new NotFoundException("Book detail id " + id + " not found");
		} else {
			return new ResponseEntity<>(bookDetail, HttpStatus.OK);
		}
	}

	private JSONObject getResponse(String message, int status_code, Object data) {
		JSONObject result = new JSONObject();
		result.put("message", message);
		result.put("status_code", status_code);
		result.put("data", data);

		return result;
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
	public ResponseEntity<List<BookDetail>> searchBook(@RequestParam("name") String name, @RequestParam(name = "page", required = false, defaultValue = Constant.DEFAULT_PAGE + "") int page, @RequestParam(name = "size", required = false, defaultValue = Constant.DEFAULT_OFFSET + "") int size) {
		Pageable pageable = PageRequest.of(page - 1, size);
		List<BookDetail> books = bookDetailsServices.searchBookDetails(name.toLowerCase(), pageable);

		return new ResponseEntity<>(books, HttpStatus.OK);
	}
}
