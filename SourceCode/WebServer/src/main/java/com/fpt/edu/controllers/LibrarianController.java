package com.fpt.edu.controllers;

import com.fpt.edu.common.enums.*;
import com.fpt.edu.common.helpers.ImportHelper;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.*;
import com.fpt.edu.exceptions.EntityNotFoundException;
import com.fpt.edu.services.*;
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
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("librarian")
public class LibrarianController extends BaseController {
	private final UserServices userServices;
	private final BookDetailsServices bookDetailsServices;
	private final BookServices bookServices;
	private final ImportHelper importHelper;
	private final MatchingServices matchingServices;
	private final RequestServices requestServices;

	@Autowired
	public LibrarianController(UserServices userServices,
							   BookDetailsServices bookDetailsServices,
							   BookServices bookServices, ImportHelper importHelper,
							   MatchingServices matchingServices, RequestServices requestServices) {
		this.userServices = userServices;
		this.bookDetailsServices = bookDetailsServices;
		this.bookServices = bookServices;
		this.importHelper = importHelper;
		this.matchingServices = matchingServices;
		this.requestServices = requestServices;
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
		@RequestParam(name = "name", required = false) String name,
		@RequestParam(name = "page", required = false, defaultValue = Constant.DEFAULT_PAGE + "") int page,
		@RequestParam(name = "size", required = false, defaultValue = Constant.DEFAULT_OFFSET + "") int size
	) {
		Pageable pageable = PageRequest.of(page - 1, size);

		List<BookDetail> bookDetailList;
		if (name != null && !name.trim().isEmpty()) {
			bookDetailList = bookDetailsServices.getAllBookDetails(pageable);
		} else {
			bookDetailList = bookDetailsServices.searchBookDetails(name, pageable);
		}

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
	public ResponseEntity<String> importData(@RequestParam("file") MultipartFile file) throws Exception {
		File f = utils.convertMultiPartToFile(file);
		String arrBook = FileUtil.readAsString(f);
		JSONArray arr = new JSONArray(arrBook);
		importHelper.initData(arr);
		importHelper.startImport();
		f.delete();
		return new ResponseEntity<>(arrBook.toString(), HttpStatus.OK);
	}

	@ApiOperation(value = "Librarian transfers book for readers", response = String.class)
	@PostMapping("/give_book")
	public ResponseEntity<String> transferBook(@RequestParam Long book_detail_id, Principal principal) throws EntityNotFoundException {
		// Check sender is librarian
		User librarian = userServices.getUserByEmail(principal.getName());

		// Check this book has matching or not
		Book book = bookServices.getByUserAndBookDetail(librarian.getId(), book_detail_id);
		if (book == null) {
			throw new EntityNotFoundException("Librarian does not have any book detail id : " + book_detail_id);
		}

		// Init response
		JSONObject jsonResult = new JSONObject();
		Date now = new Date();

		// Check librarian has created a matching instance in DB or not
		Matching existedMatching = matchingServices.getByBookId(
			book.getId(),
			EMatchingStatus.PAIRED.getValue(),
			EMatchingStatus.PENDING.getValue());
		if (existedMatching != null) {
			long duration = utils.getDuration(existedMatching.getMatchingStartDate(), now, TimeUnit.MINUTES);

			// If pin is expired
			if (duration > Constant.PIN_EXPIRED_MINUTE) {
				// Update matching
				String pin = getUniquePin();
				existedMatching.setPin(pin);
				existedMatching.setMatchingStartDate(now);
				matchingServices.updateMatching(existedMatching);
			}

			//Return response to client
			jsonResult.put("pin", existedMatching.getPin());
			jsonResult.put("matching_id", existedMatching.getId());
			jsonResult.put("request_id", existedMatching.getReturnerRequest().getId());
			jsonResult.put("created_at", existedMatching.getMatchingStartDate().getTime());
			return new ResponseEntity<>(jsonResult.toString(), HttpStatus.OK);
		}

		// Init return request
		Request returningRequest = new Request();
		returningRequest.setBook(book);
		returningRequest.setUser(librarian);
		returningRequest.setStatus(ERequestStatus.PENDING.getValue());
		returningRequest.setType(ERequestType.RETURNING.getValue());
		returningRequest.setMode(ERequestMode.MANUAL.getValue());
		Request savedRequest = requestServices.saveRequest(returningRequest);
		Long requestId = savedRequest.getId();

		// Init pin
		String pin = getUniquePin();

		// Init matching
		Matching matching = new Matching();
		matching.setReturnerRequest(returningRequest);
		matching.setBook(book);
		matching.setPin(pin);
		matching.setMatchingStartDate(now);
		matching.setStatus(EMatchingStatus.PENDING.getValue());
		Matching savedMatching = matchingServices.saveMatching(matching);
		Long matchingId = savedMatching.getId();

		// Update book transfer status
		book.setTransferStatus(EBookTransferStatus.TRANSFERRING.getValue());
		bookServices.updateBook(book);

		// return response to client
		jsonResult.put("pin", pin);
		jsonResult.put("matching_id", matchingId);
		jsonResult.put("request_id", requestId);
		jsonResult.put("created_at", now.getTime());
		return new ResponseEntity<>(jsonResult.toString(), HttpStatus.OK);
	}

	// Get unique pin
	private String getUniquePin() {
		// Check whether pin is duplicated or not
		String pin;
		Matching duplicatedMat;
		do {
			pin = utils.getPin();
			duplicatedMat = matchingServices.getByPin(
				pin,
				EMatchingStatus.PAIRED.getValue(),
				EMatchingStatus.PENDING.getValue());
		} while (duplicatedMat != null);

		return pin;
	}

}
