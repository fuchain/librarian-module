package com.fpt.edu.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.edu.common.helpers.ImportHelper;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.Book;
import com.fpt.edu.entities.BookDetail;
import com.fpt.edu.entities.User;
import com.fpt.edu.services.BookDetailsServices;
import com.fpt.edu.services.BookServices;
import com.fpt.edu.services.UserServices;
import io.swagger.annotations.ApiOperation;
import org.aspectj.util.FileUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.util.List;

@RestController
@RequestMapping("librarian")
public class LibrarianController extends BaseController {
	private final UserServices userServices;
	private final BookDetailsServices bookDetailsServices;
	private final BookServices bookServices;

	@Autowired
	public LibrarianController(UserServices userServices,
							   BookDetailsServices bookDetailsServices,
							   BookServices bookServices) {
		this.userServices = userServices;
		this.bookDetailsServices = bookDetailsServices;
		this.bookServices = bookServices;

	}

	@ApiOperation("Get all users")
	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUsers(
		@RequestParam(name = "page", required = false, defaultValue = Constant.DEFAULT_PAGE + "") int page,
		@RequestParam(name = "size", required = false, defaultValue = Constant.DEFAULT_OFFSET + "") int size
	) {
		Pageable pageable = PageRequest.of(page - 1, size);
		List<User> userList = userServices.getAllUsers(pageable);
		return new ResponseEntity<>(userList, HttpStatus.OK);
	}

	@ApiOperation("Get current book list by user id")
	@GetMapping("/users/{id}/books")
	public ResponseEntity<List<Book>> getBookByUserId(
		@PathVariable Long id,
		@RequestParam(name = "page", required = false, defaultValue = Constant.DEFAULT_PAGE + "") int page,
		@RequestParam(name = "size", required = false, defaultValue = Constant.DEFAULT_OFFSET + "") int size
	) {
		Pageable pageable = PageRequest.of(page - 1, size);
		userServices.getByUserId(id);
		List<Book> bookList = userServices.getCurrentBookListOfUser(id);
		return new ResponseEntity<>(bookList, HttpStatus.OK);
	}

	@ApiOperation(value = "Disable a user", response = User.class)
	@PutMapping("/users/{id}")
	public ResponseEntity<User> disableUser(@PathVariable Long id, @RequestParam("disable") boolean disable) {
		User user = userServices.getByUserId(id);

		user.setDisabled(disable);
		User updatedUser = userServices.updateUser(user);

		return new ResponseEntity<>(updatedUser, HttpStatus.OK);
	}

	@ApiOperation(value = "Get list of all book details", response = List.class)
	@GetMapping("/book_details")
	@Transactional
	public ResponseEntity<String> getListBookDetails(
		@RequestParam(name = "page", required = false, defaultValue = Constant.DEFAULT_PAGE + "") int page,
		@RequestParam(name = "size", required = false, defaultValue = Constant.DEFAULT_OFFSET + "") int size
	) {
		Pageable pageable = PageRequest.of(page - 1, size);
		List<BookDetail> bookDetailList = bookDetailsServices.getAllBookDetails(pageable);
		JSONArray arrResult = new JSONArray();
		for (BookDetail bookDetail : bookDetailList) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(Constant.ID, bookDetail.getId());
			jsonObject.put(Constant.NAME, bookDetail.getName());
			jsonObject.put(Constant.UPDATE_DATE, String.valueOf(bookDetail.getUpdateDate().getTime() / 1000));
			jsonObject.put(Constant.CREATE_DATE, String.valueOf(bookDetail.getUpdateDate().getTime() / 1000));
			jsonObject.put("bookInstanceCount", bookDetail.getBooks().size());
			arrResult.put(jsonObject);
		}
		return new ResponseEntity<>(arrResult.toString(), HttpStatus.OK);
	}

	@ApiOperation(value = "Get list instances of a book detail", response = List.class)
	@GetMapping("/book_details/{bookdetail_id}/books")
	public ResponseEntity<List<Book>> getListBookInstances(
		@PathVariable("bookdetail_id") Long bookDetailId,
		@RequestParam(name = "transfer_status", required = false) String transferStatus,
		@RequestParam(name = "page", required = false, defaultValue = Constant.DEFAULT_PAGE + "") int page,
		@RequestParam(name = "size", required = false, defaultValue = Constant.DEFAULT_OFFSET + "") int size
	) {

		Pageable pageable = PageRequest.of(page - 1, size);
		if (transferStatus == null) {
			return new ResponseEntity<>(
				bookServices.getListBookByBookDetailId(bookDetailId, pageable),
				HttpStatus.OK
			);
		} else {
			return new ResponseEntity<>(
				bookServices.getListBookByBookDetailIdWithFilter(bookDetailId, transferStatus, pageable),
				HttpStatus.OK
			);
		}
	}

	@ApiOperation(value = "Get history of book instance", response = Book.class)
	@GetMapping("/books/{book_id}")
	public ResponseEntity<Book> getHistoryOfBookInstance(
		@PathVariable("book_id") Long bookId
	) throws Exception {
		Book book = bookServices.getBookById(bookId);
		bookServices.getListTransactionsFromBigchain(book);
		return new ResponseEntity<>(book, HttpStatus.OK);
	}

	@ApiOperation(value = "Get history of book instance", response = Book.class)
	@PostMapping("/bookDetails/import")
	public ResponseEntity<String> importData(@RequestParam("file")MultipartFile file) throws Exception {
		File f = utils.convertMultiPartToFile(file);
		String arrBook = FileUtil.readAsString(f);
		JSONArray arr = new JSONArray(arrBook);
		ImportHelper myHelper = new ImportHelper(arr);
		myHelper.startImport();

		return new ResponseEntity<>(arrBook.toString(), HttpStatus.OK);


	}


}
