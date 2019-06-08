package com.fpt.edu.controller;

import com.fpt.edu.entities.Book;
import com.fpt.edu.entities.BookDetail;
import com.fpt.edu.entities.User;
import com.fpt.edu.services.BookDetailsServices;
import com.fpt.edu.services.BookServices;
import com.fpt.edu.services.UserServices;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> userList = userServices.getAllUsers();
		return new ResponseEntity<>(userList, HttpStatus.OK);
	}

	@ApiOperation("Get current book list by user id")
	@GetMapping("/users/{id}/books")
	public ResponseEntity<List<Book>> getBookByUserId(@PathVariable Long id) {
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
	public ResponseEntity<List<BookDetail>> getListBookDetails() {
		// Do we need authentication here???
		return new ResponseEntity<>(bookDetailsServices.getAllBookDetails(), HttpStatus.OK);
	}

	@ApiOperation(value = "Get list instances of a book detail", response = List.class)
	// need to identify specific class
	@GetMapping("/{id}/books")
	public ResponseEntity<List<Book>> getListBookInstances(@PathVariable Long id) {
		// Do we need authentication here???
		return new ResponseEntity<>(bookServices.getListBookByBookDetailId(id), HttpStatus.OK);
	}

}
