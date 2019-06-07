package com.fpt.edu.controller;

import com.fpt.edu.entities.Book;
import com.fpt.edu.entities.User;
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

	@Autowired
	public LibrarianController(UserServices userServices) {
		this.userServices = userServices;
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
}
