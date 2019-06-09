package com.fpt.edu.controller;

import com.bigchaindb.constants.Operations;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.Book;
import com.fpt.edu.entities.BookDetail;
import com.fpt.edu.entities.User;
import com.fpt.edu.services.BigchainTransactionServices;
import com.fpt.edu.services.BookDetailsServices;
import com.fpt.edu.services.BookServices;
import com.fpt.edu.services.UserServices;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("librarian")
public class LibrarianController extends BaseController {
	private final UserServices userServices;
	private final BookDetailsServices bookDetailsServices;
	private final BookServices bookServices;
	private final BigchainTransactionServices bigchainTransactionServices;

	@Autowired
	public LibrarianController(UserServices userServices,
							   BookDetailsServices bookDetailsServices,
							   BookServices bookServices,
							   BigchainTransactionServices bigchainTransactionServices) {
		this.userServices = userServices;
		this.bookDetailsServices = bookDetailsServices;
		this.bookServices = bookServices;
		this.bigchainTransactionServices = bigchainTransactionServices;

	}

	@ApiOperation("Get all users")
	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUsers(
		 @RequestParam(name = "page", required = false, defaultValue = Constant.DEFAULT_PAGE+"") int page,
		 @RequestParam(name = "size", required = false,defaultValue = Constant.DEFAULT_OFFSET+"") int size ) {
		Pageable pageable = PageRequest.of(page-1,size);
		List<User> userList = userServices.getAllUsers(pageable);
		return new ResponseEntity<>(userList, HttpStatus.OK);
	}

	@ApiOperation("Get current book list by user id")
	@GetMapping("/users/{id}/books")
	public ResponseEntity<List<Book>> getBookByUserId(
		@PathVariable Long id,@RequestParam(name = "page", required = false, defaultValue = Constant.DEFAULT_PAGE+"") int page,
		@RequestParam(name = "size", required = false,defaultValue = Constant.DEFAULT_OFFSET+"") int size ) {
		Pageable pageable = PageRequest.of(page-1,size);
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
	// need to identify specific class
	@GetMapping("/book_details")
	public ResponseEntity<List<BookDetail>> getListBookDetails(
		@RequestParam(name = "page", required = false, defaultValue = Constant.DEFAULT_PAGE+"") int page,
		@RequestParam(name = "size", required = false,defaultValue = Constant.DEFAULT_OFFSET+"") int size ) {
		// Do we need authentication here???
		Pageable pageable = PageRequest.of(page-1,size);
		return new ResponseEntity<>(bookDetailsServices.getAllBookDetails(pageable), HttpStatus.OK);
	}

	@ApiOperation(value = "Get list instances of a book detail", response = List.class)
	// need to identify specific class
	@GetMapping("/book_details/{bookdetail_id}/books")
	public ResponseEntity<List<Book>> getListBookInstances(
		@PathVariable("bookdetail_id") Long bookDetailId,
		@RequestParam(name = "page", required = false, defaultValue = Constant.DEFAULT_PAGE+"") int page,
		@RequestParam(name = "size", required = false,defaultValue = Constant.DEFAULT_OFFSET+"") int size ) {
		// Do we need authentication here???
		Pageable pageable = PageRequest.of(page-1,size);
		return new ResponseEntity<>(bookServices.getListBookByBookDetailId(bookDetailId,pageable), HttpStatus.OK);
	}

	@ApiOperation(value = "Get history of book instance", response = Book.class)
	@GetMapping("/books/{book_id}")
	public ResponseEntity<Book> getHistoryOfBookInstance(@PathVariable("book_id") Long bookId) throws Exception {
		// Do we need authentication here???
		Book book = bookServices.getBookById(bookId);
		book.setBcTransactions(
			bigchainTransactionServices.getTransactionsByAssetId(
				book.getAssetId(),
				Operations.TRANSFER
			));
		return new ResponseEntity<>(book, HttpStatus.OK);
	}
}
