package com.fpt.edu.controller;

import com.fpt.edu.common.enums.ERequestStatus;
import com.fpt.edu.common.enums.ERequestType;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.entity.Book;
import com.fpt.edu.entity.Request;
import com.fpt.edu.entity.User;
import com.fpt.edu.service.RequestServices;
import com.fpt.edu.service.UserServices;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController extends BaseController {
	private final UserServices userServices;
	private final RequestServices requestServices;

	@Autowired
	public UserController(UserServices userServices, RequestServices requestServices) {
		this.userServices = userServices;
		this.requestServices = requestServices;
	}

	@ApiOperation(value = "Get a list of current book")
	@GetMapping("current_books")
	public ResponseEntity<List<Book>> getCurrentBook(Principal principal) {
		// Get user information
		User user = userServices.getUserByEmail(principal.getName());

		// Get current book list of user
		List<Book> currentBookList = userServices.getCurrentBookListOfUser(user.getId());

		// Get book list that is returning
		List<Request> returningList = requestServices.findByUserIdAndType(
			user.getId(),
			ERequestType.RETURNING.getValue(),
			ERequestStatus.PENDING.getValue(),
			ERequestStatus.MATCHING.getValue()
		);

		// Remove books that is returning from current book list
		Iterator iterator = currentBookList.iterator();
		while (iterator.hasNext()) {
			Book currentBook = (Book) iterator.next();

			for (Request request : returningList) {
				Book returningBook = request.getBook();

				if (currentBook.getId().equals(returningBook.getId())) {
					iterator.remove();
				}
			}
		}
		return new ResponseEntity<>(currentBookList, HttpStatus.OK);
	}

	static ResponseEntity<String> getJSONResponseUserProfile(User user) {
		JSONObject responseJSON = new JSONObject();
		responseJSON.put("id", user.getId());
		responseJSON.put("email", user.getEmail());
		responseJSON.put("fullname", user.getFullName());
		responseJSON.put("phone", user.getPhone());

		return ResponseEntity.ok().body(responseJSON.toString());
	}

	@ApiOperation(value = "Get user information", response = String.class)
	@GetMapping("profile")
	public ResponseEntity<String> getMe(Principal principal) {
		String email = principal.getName();
		User user = userServices.getUserByEmail(email);

		return getJSONResponseUserProfile(user);
	}

	@ApiOperation(value = "Update user profile", response = String.class)
	@RequestMapping(value = "/update_profile", method = RequestMethod.PUT, produces = Constant.APPLICATION_JSON)
	public ResponseEntity<User> updateUser(@RequestBody String body, Principal principal) throws Exception {
		// Get user information
		User user = userServices.getUserByEmail(principal.getName());

		// Get full name and phone from Request Body
		JSONObject bodyObject = new JSONObject(body);
		String fullName = bodyObject.getString("fullname");
		String phone = bodyObject.getString("phone");

		// Check phone number limit is 10 or not
		if (phone.length() != Constant.PHONE_NUMBER) {
			throw new Exception("Phone number must be 10 digits");
		}

		// Check all characters in phone is digit or not
		for (char ch : phone.toCharArray()) {
			if (!Character.isDigit(ch)) {
				throw new Exception("Phone number must be digits");
			}
		}

		// Set value for user object
		user.setFullName(fullName);
		user.setPhone(phone);

		// Update user in DB
		User updatedUser = userServices.updateUser(user);

		return new ResponseEntity<>(updatedUser, HttpStatus.OK);
	}


	@ApiOperation(value = "Get user's book info", response = String.class)
	@RequestMapping(value = "/book_infos", method = RequestMethod.GET, produces = Constant.APPLICATION_JSON)
	public ResponseEntity<String> getUserBookInfo(Principal principal) {

		User user = userServices.getUserByEmail(principal.getName());
		// Get number of current book list of user
		int numberOfKeepingBook = userServices.countNumberOfBookThatUserKeep(user.getId());
		// Get book list that is returning
		int numberOfBorrowRequest = requestServices.countPendingRequestOfUser(
			user.getId(),
			ERequestType.BORROWING.getValue(),
			ERequestStatus.PENDING.getValue(),
			ERequestStatus.MATCHING.getValue()
		);

		int numberOfReturnRequest = requestServices.countPendingRequestOfUser(
			user.getId(),
			ERequestType.RETURNING.getValue(),
			ERequestStatus.PENDING.getValue(),
			ERequestStatus.MATCHING.getValue()
		);
		JSONObject response = new JSONObject();
		response.put(Constant.NUM_OF_KEEP_BOOK, numberOfKeepingBook - numberOfReturnRequest);
		response.put(Constant.NUM_OF_RETURNING_BOOK, numberOfReturnRequest);
		response.put(Constant.NUM_OF_REQUESTING_BOOK, numberOfBorrowRequest);
		return new ResponseEntity<>(response.toString(), HttpStatus.OK);
	}

}
