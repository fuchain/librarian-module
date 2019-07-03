package com.fpt.edu.controllers;

import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fpt.edu.common.enums.*;
import com.fpt.edu.common.helpers.ImageHelper;
import com.fpt.edu.common.helpers.ImportHelper;
import com.fpt.edu.common.request_queue_simulate.Message;
import com.fpt.edu.common.request_queue_simulate.PublishSubscribe;
import com.fpt.edu.common.request_queue_simulate.RequestQueueManager;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.*;
import com.fpt.edu.exceptions.*;
import com.fpt.edu.services.*;
import io.swagger.annotations.ApiOperation;
import org.hibernate.Hibernate;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("requests")
public class RequestController extends BaseController {
	public RequestController(UserServices userServices, BookDetailsServices bookDetailsServices,
							 BookServices bookServices, ImportHelper importHelper, MatchingServices matchingServices,
							 RequestServices requestServices,
							 PublishSubscribe publishSubscribe, RequestQueueManager requestQueueManager,
							 NotificationService notificationService) {
		super(userServices, bookDetailsServices, bookServices, importHelper, matchingServices, requestServices, publishSubscribe, requestQueueManager, notificationService);
	}

	@ApiOperation(value = "Get a request by its id")
	@GetMapping(value = "/{id}")
	public ResponseEntity<Request> getRequestById(@PathVariable Long id) {
		Request request = requestServices.getRequestById(id);

		return new ResponseEntity<>(request, HttpStatus.OK);
	}

	@ApiOperation(value = "Get a list of book request")
	@GetMapping("/get_list")
	@Transactional
	public ResponseEntity<List<Request>> getBookRequestList(@RequestParam int type, Principal principal) {
		// Get user information
		User user = userServices.getUserByEmail(principal.getName());

		// Get book list of user, by default lazy loading is enable
		Hibernate.initialize(user.getListBooks());

		// Get all requests based on user id and request type, with status not completed
		List<Request> requestList = requestServices.findByUserIdAndType(
			user.getId(),
			type,
			ERequestStatus.PENDING.getValue(),
			ERequestStatus.MATCHING.getValue()
		);

		// Get paired user information from the request whose status is matching
		for (Request r : requestList) {
			if (r.getStatus() == ERequestStatus.MATCHING.getValue()) {
				if (r.getType() == ERequestType.RETURNING.getValue()) {

					// Get matching based on request id, matching status has not confirmed yet
					Matching matching = matchingServices.getByReturnRequestId(
						r.getId(),
						EMatchingStatus.PAIRED.getValue(),
						EMatchingStatus.PENDING.getValue());

					// Set paired user information for request
					User borrower = matching.getBorrowerRequest().getUser();
					r.setPairedUser(borrower);
				} else if (r.getType() == ERequestType.BORROWING.getValue()) {

					// Get matching based on request id, matching status has not confirmed yet
					Matching matching = matchingServices.getByReceiveRequestId(
						r.getId(),
						EMatchingStatus.PAIRED.getValue(),
						EMatchingStatus.PENDING.getValue());

					// Set paired user information for request
					User returner = matching.getReturnerRequest().getUser();
					r.setPairedUser(returner);
				}
			}
		}
		return new ResponseEntity<>(requestList, HttpStatus.OK);
	}

	@ApiOperation(value = "Create a book request", response = String.class)
	@PostMapping("")
	@Transactional
	public ResponseEntity<String> requestBook(@RequestBody String body, Principal principal) throws Exception {
		// Get user information
		User user = userServices.getUserByEmail(principal.getName());

		//Get type from Request Body
		JSONObject bodyObject = new JSONObject(body);
		int type = bodyObject.getInt("type");

		// Init a request to insert to DB
		Request request;

		// Fill data for request
		if (type == ERequestType.BORROWING.getValue()) {
			// Check user is active or not
			if (user.isDisabled() == null || user.isDisabled()) {
				return new ResponseEntity<>("User id: " + user.getId() + " is not active. Cannot make borrow request", HttpStatus.BAD_REQUEST);
			}

			// Get book name from Request Body
			String bookName = bodyObject.getString("book_name");

			request = getBorrowingRequest(type, user, bookName);
		} else if (type == ERequestType.RETURNING.getValue()) {
			// Get book id from Request Body
			Long bookId = bodyObject.getLong("book_id");

			// Update transfer status of book
			Book book = bookServices.getBookById(bookId);

			if (book.getStatus().equals(EBookStatus.LOCKED.getValue())) {
				return new ResponseEntity<>("Book status is locked, cannot make a request to return", HttpStatus.BAD_REQUEST);
			}

			book.setTransferStatus(EBookTransferStatus.TRANSFERRING.getValue());
			bookServices.updateBook(book);

			request = getReturningRequest(type, user, bookId);
		} else {
			return new ResponseEntity<>("Type " + type + " is not supported", HttpStatus.BAD_REQUEST);
		}

		// Set mode of request
		request.setMode(ERequestMode.AUTOMATIC.getValue());

		// Find the matching request
		pairRequest(request);

//        requestServices.saveRequest(request);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("message", "success");
		return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
	}

	private Request getBorrowingRequest(int type, User user, String bookName)
		throws Exception {
		// Get book detail object from DB
		BookDetail bookDetail = bookDetailsServices.getBookDetailByName(bookName);
		if (bookDetail == null) {
			throw new EntityNotFoundException("Book name: " + bookName + " not found");
		}

		// Check existed request based on request type, user id, book detail, with request status is not completed
		boolean existed = requestServices.checkExistedRequest(
			type, user.getId(),
			ERequestStatus.PENDING.getValue(), ERequestStatus.MATCHING.getValue(),
			bookDetail.getId(), (long) 0
		);
		if (existed) {
			throw new EntityAldreayExisted("Request's already existed");
		}

		// Check user is making a request to borrow books that the user currently has
		List<Book> currentBookList = userServices.getCurrentBookListOfUser(user.getId());
		for (Book b : currentBookList) {
			if (b.getBookDetail().getName().equals(bookName)) {
				throw new Exception("You currently have " + bookName + ". You cannot make a borrow request for this book");
			}
		}

		//create a request and fill data
		Request request = new Request();
		request.setStatus(ERequestStatus.PENDING.getValue());
		request.setType(type);
		request.setUser(user);
		request.setBookDetail(bookDetail);

		return request;
	}

	private Request getReturningRequest(int type, User user, Long bookId)
		throws EntityNotFoundException, EntityAldreayExisted {
		//get book object
		Book book = bookServices.getBookById(bookId);
		BookDetail bookDetail = book.getBookDetail();

		//check whether user is keeping this book
		boolean keeping = false;
		List<Book> currentBookList = userServices.getCurrentBookListOfUser(user.getId());
		for (Book b : currentBookList) {
			if (b.getId().equals(bookId)) {
				keeping = true;
				break;
			}
		}
		//if user's not keeping this book but want to return book, throw exceptions
		if (!keeping) {
			throw new EntityNotFoundException("This book is not found in the current book list");
		}

		//check request existed based on request type, user id, book id, with request status is not completed
		boolean existed = requestServices.checkExistedRequest(
			type, user.getId(),
			ERequestStatus.PENDING.getValue(), ERequestStatus.MATCHING.getValue(),
			(long) 0, book.getId()
		);
		if (existed) {
			throw new EntityAldreayExisted("Request's already existed");
		}

		//create a request and fill data
		Request request = new Request();
		request.setStatus(ERequestStatus.PENDING.getValue());
		request.setType(type);
		request.setUser(user);
		request.setBook(book);
		request.setBookDetail(bookDetail);

		return request;
	}

	private void pairRequest(Request request) throws NotFoundException {
		Message message = new Message();
		message.setMessage(request);
		message.setAction(Constant.ACTION_ADD_NEW);

		publishSubscribe.setMessage(message);
		publishSubscribe.notifyToSub();

		Request matchRequest = requestQueueManager.findTheMatch(request);
		if (matchRequest != null) {
			//remove request
			requestQueueManager.removeRequestOutTheQueue(request);
			// update for request
			message.setAction(Constant.ACTION_UPDATE);
			request.setStatus(ERequestStatus.MATCHING.getValue());
			publishSubscribe.notifyToSub();
			matchRequest.setStatus(ERequestStatus.MATCHING.getValue());

			Message matchMessage = new Message();
			matchMessage.setAction(Constant.ACTION_UPDATE);
			matchMessage.setMessage(matchRequest);
			publishSubscribe.setMessage(matchMessage);
			publishSubscribe.notifyToSub();

			Matching matching = new Matching();
			matching.setStatus(EMatchingStatus.PAIRED.getValue());

			if (matchRequest.getBook() != null) {
				matching.setBook(matchRequest.getBook());
			} else if (request.getBook() != null) {
				matching.setBook(request.getBook());
			}

			if (request.getType() == ERequestType.BORROWING.getValue()) {
				matching.setBorrowerRequest(request);
				matching.setReturnerRequest(matchRequest);
			} else {
				matching.setBorrowerRequest(matchRequest);
				matching.setReturnerRequest(request);
			}
			matchingServices.updateMatching(matching);

			// Push notification to returner and receiver
			pushNotiFromRequest(request, matchRequest);
			pushNotiFromRequest(matchRequest, request);
		}
	}

	private void pushNotiFromRequest(Request request, Request matchRequest) {
		if (request.getType() == ERequestType.RETURNING.getValue()) {
			notificationService.pushNotification(
				request.getUser().getEmail(),
				"Yêu cầu trả sách " + request.getBook().getBookDetail().getName() + " đã được ghép với " + matchRequest.getUser().getFullName(),
				Constant.NOTIFICATION_TYPE_RETURNING
			);
		} else {
			notificationService.pushNotification(
				request.getUser().getEmail(),
				"Yêu cầu mượn sách " + request.getBookDetail().getName() + " đã được ghép với " + matchRequest.getUser().getFullName(),
				Constant.NOTIFICATION_TYPE_REQUESTING
			);
		}
	}

	@ApiOperation(value = "Transfer book for returner and receiver", response = String.class)
	@PutMapping(value = "/transfer", produces = Constant.APPLICATION_JSON)
	public ResponseEntity<String> transferBook(@RequestBody String body, Principal principal) throws Exception {
		// Get user information
		User sender = userServices.getUserByEmail(principal.getName());

		// Get data from Request Body
		JSONObject bodyObject = new JSONObject(body);
		int type = bodyObject.getInt("type");
		Long matchingId = bodyObject.getLong("matchingId");

		// Init JSONObject to return response
		JSONObject jsonResult;

		// Check whether matchingId is existed or not
		Matching matching = matchingServices.getMatchingById(matchingId);
		if (matching == null) {
			return new ResponseEntity<>("Matching id: " + matchingId + " not found", HttpStatus.BAD_REQUEST);
		}

		// Check transfer type is returner or receiver
		if (type == ETransferType.RETURNER.getValue()) {
			jsonResult = returnBook(matching, sender);

			return new ResponseEntity<>(jsonResult.toString(), HttpStatus.OK);
		} else if (type == ETransferType.RECEIVER.getValue()) {
			String pin = bodyObject.getString("pin");
			jsonResult = receiveBook(matching, sender, pin);

			// Check submit transaction to BC success or not
			if (jsonResult.get("status_code").equals(HttpStatus.OK)) {
				jsonResult.remove("status_code");
				return new ResponseEntity<>(jsonResult.toString(), HttpStatus.OK);
			} else {
				jsonResult.remove("status_code");
				return new ResponseEntity<>(jsonResult.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			return new ResponseEntity<>("Type: " + type + " is not supported", HttpStatus.BAD_REQUEST);
		}
	}

	private JSONObject returnBook(Matching matching, User sender) throws EntityIdMismatchException {
		User matchingUser = matching.getReturnerRequest().getUser();
		if (matchingUser.getId().longValue() != sender.getId().longValue()) {
			throw new EntityIdMismatchException("User id: " + matchingUser.getId() + " does not match with user id from authentication");
		}

		// Update matching fields(pin, matchingDate, status)
		String generatedPin = utils.getPin();
		Date createdAt = new Date();
		int status = EMatchingStatus.PENDING.getValue();

		matching.setPin(generatedPin);
		matching.setMatchingStartDate(createdAt);
		matching.setStatus(status);

		matchingServices.updateMatching(matching);

		// Response to user
		JSONObject jsonResult = new JSONObject();
		jsonResult.put("pin", generatedPin);
		jsonResult.put("created_at", createdAt);
		jsonResult.put("status", status);

		return jsonResult;
	}

	private JSONObject receiveBook(Matching matching, User sender, String pin)
		throws Exception {
		// Check borrower is the person who sends request or not
		User matchingUser = matching.getBorrowerRequest().getUser();
		if (matchingUser.getId().longValue() != sender.getId().longValue()) {
			throw new EntityIdMismatchException("User id: " + matchingUser.getId() + " does not match with user id from authentication");
		}

		// Check whether matching status equals completed or not
		if (matching.getStatus() == ERequestStatus.COMPLETED.getValue()) {
			throw new EntityAldreayExisted("The pin has have sent");
		}

		// Check expired time of pin
		long duration = utils.getDuration(matching.getMatchingStartDate(), new Date(), TimeUnit.MINUTES);
		if (duration > Constant.PIN_EXPIRED_MINUTE) {
			throw new PinExpiredException("Pin: " + pin + " has been expired");
		}
		// Check user enter the correct pin or not
		if (!matching.getPin().equals(pin)) {
			throw new EntityPinMisMatchException("Pin: " + pin + " does not match to Matching");
		}

		return addTransferTxToBC(matching);
	}

	// Add transaction to BC, used for AUTOMATIC transfer
	private JSONObject addTransferTxToBC(Matching matching) throws Exception {
		// Init data to submit transaction to BlockChain
		Book book = bookServices.getBookById(matching.getBook().getId());
		Request returnerRequest = matching.getReturnerRequest();
		Request receiverRequest = matching.getBorrowerRequest();
		User returner = returnerRequest.getUser();
		User receiver = receiverRequest.getUser();
		Date sendTime = new Date();
		AtomicBoolean callback = new AtomicBoolean(false);
		JSONObject jsonResult = new JSONObject();

		// Set default value for response
		jsonResult.put("message", "failed to submit transaction");
		jsonResult.put("status_code", HttpStatus.INTERNAL_SERVER_ERROR);

		// Update current_keeper
		book.setUser(receiver);

		BookMetadata bookMetadata = book.getMetadata();
		bookMetadata.setStatus(book.getStatus());
		bookMetadata.setTransactionTimestamp(String.valueOf(System.currentTimeMillis() / 1000));
		// Submit transaction to BlockChain
		BigchainTransactionServices services = new BigchainTransactionServices();
		services.doTransfer(
			book.getLastTxId(),
			book.getAssetId(), bookMetadata.getData(),
			String.valueOf(returner.getEmail()), String.valueOf(receiver.getEmail()),
			(transaction, response) -> { // Success

				// Update last transaction id for book
				String transactionId = transaction.getId();
				book.setLastTxId(transactionId);

				// Update status of request to "completed"
				returnerRequest.setStatus(ERequestStatus.COMPLETED.getValue());
				receiverRequest.setStatus(ERequestStatus.COMPLETED.getValue());
				requestServices.updateRequest(returnerRequest);
				requestServices.updateRequest(receiverRequest);

				// Update status of matching
				matching.setStatus(EMatchingStatus.CONFIRMED.getValue());
				matchingServices.updateMatching(matching);

				// Update transfer status of book
				book.setTransferStatus(EBookTransferStatus.TRANSFERRED.getValue());
				bookServices.updateBook(book);

				// Override value in response
				jsonResult.put("message", "confirm book transfer successfully");
				jsonResult.put("status_code", HttpStatus.OK);

				callback.set(true);
			},
			(transaction, response) -> { //failed
				// Update request + matching status to 'Canceled'
				matching.setStatus(EMatchingStatus.CANCELED.getValue());
				matchingServices.updateMatching(matching);

				returnerRequest.setStatus(ERequestStatus.CANCELED.getValue());
				receiverRequest.setStatus(ERequestStatus.CANCELED.getValue());
				requestServices.updateRequest(returnerRequest);
				requestServices.updateRequest(receiverRequest);

				// Update user in book
				book.setUser(returner);
				book.setTransferStatus(EBookTransferStatus.TRANSFERRED.getValue());
				bookServices.updateBook(book);

				notificationService.pushNotification(
					Constant.LIBRARIAN_EMAIL,
					"Transaction chuyển sách của sách có ID là " + book.getId() + " của " + returner.getEmail() + " và " + receiver.getEmail() + " đã thất bại",
					Constant.NOTIFICATION_TYPE_BOOKINSTANCE + ":" + book.getId()
				);

				callback.set(true);
			}
		);

		// Wait until the BigchainTransactionServices has callback or request is timeout
		Date now;
		while (true) {
			now = new Date();
			long duration = utils.getDuration(sendTime, now, TimeUnit.SECONDS);

			if (duration > Constant.BIGCHAIN_REQUEST_TIMEOUT || callback.get()) {
				return jsonResult;
			}
		}
	}

	@ApiOperation(value = "Update request", response = String.class)
	@PutMapping("/{id}")
	public ResponseEntity<String> updateRequest(@PathVariable Long id, @RequestBody Request request)
		throws EntityNotFoundException, EntityIdMismatchException {
		if (!request.getId().equals(id)) {
			throw new EntityIdMismatchException("Request id: " + id + " and " + request.getId() + " is not matched");
		}

		Request existedRequest = requestServices.getRequestById(request.getId());
		if (existedRequest == null) {
			throw new EntityNotFoundException("Request id: " + request + " not found");
		}

		Request requestResult = requestServices.updateRequest(request);
		JSONObject jsonObject = new JSONObject(requestResult);

		return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
	}

	@ApiOperation(value = "Get Match ID", response = JSONObject.class)
	@GetMapping("/{id}/matched")
	public ResponseEntity<String> getMatchedIdOfRequest(@PathVariable Long id) throws Exception {
		Matching matched = matchingServices.getByRequestId(id, EMatchingStatus.PAIRED.getValue(), EMatchingStatus.PENDING.getValue());
		JSONObject jsonResponse = new JSONObject();
		if (matched != null) {
			jsonResponse.put("matching_id", matched.getId());
		} else {
			throw new Exception("Cannot find matching_id with that request_id");
		}
		return new ResponseEntity<>(jsonResponse.toString(), HttpStatus.OK);
	}

	@ApiOperation(value = "Return book manually", response = String.class)
	@PostMapping(value = "/manually", produces = Constant.APPLICATION_JSON)
	public ResponseEntity<String> returnBookManually(@RequestBody String body, Principal principal) {
		// Get user information
		User user = userServices.getUserByEmail(principal.getName());

		// Get book
		JSONObject bodyObject = new JSONObject(body);
		Long bookId = bodyObject.getLong("book_id");
		Book book = bookServices.getBookById(bookId);

		// Init data
		JSONObject jsonResult = new JSONObject();
		Date now = new Date();

		// Check user has created a matching instance in DB or not
		Matching existedMatching = matchingServices.getByBookId(
			bookId,
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
			// Return pin to client
			jsonResult.put("pin", existedMatching.getPin());
			jsonResult.put("matching_id", existedMatching.getId());
			jsonResult.put("request_id", existedMatching.getReturnerRequest().getId());
			jsonResult.put("created_at", existedMatching.getMatchingStartDate().getTime());
			return new ResponseEntity<>(jsonResult.toString(), HttpStatus.OK);
		}

		// Init a returning request
		Request returningRequest = new Request();
		returningRequest.setBook(book);
		returningRequest.setUser(user);
		returningRequest.setStatus(ERequestStatus.MATCHING.getValue());
		returningRequest.setType(ERequestType.RETURNING.getValue());
		returningRequest.setMode(ERequestMode.MANUAL.getValue());
		Request savedRequest = requestServices.saveRequest(returningRequest);

		// Get request id
		Long requestId = savedRequest.getId();

		// Get unique pin
		String pin = getUniquePin();

		// Init matching instance
		Matching matching = new Matching();
		matching.setReturnerRequest(returningRequest);
		matching.setBook(book);
		matching.setPin(pin);
		matching.setMatchingStartDate(now);
		matching.setStatus(EMatchingStatus.PENDING.getValue());
		Matching savedMatching = matchingServices.saveMatching(matching);
		Long matchingId = savedMatching.getId();

		// Update transfer status of book
		book.setTransferStatus(EBookTransferStatus.TRANSFERRING.getValue());
		bookServices.updateBook(book);

		// Return response to client
		jsonResult.put("pin", pin);
		jsonResult.put("matching_id", matchingId);
		jsonResult.put("request_id", requestId);
		jsonResult.put("created_at", now.getTime());
		return new ResponseEntity<>(jsonResult.toString(), HttpStatus.OK);
	}

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

	@ApiOperation(value = "Receiver verify the book information", response = Book.class)
	@GetMapping("/manually/verify")
	public ResponseEntity<Book> verifyBookManually(@RequestParam String pin, Principal principal) throws Exception {
		// Check the receiver is active or not
		User receiver = userServices.getUserByEmail(principal.getName());
		if (receiver.isDisabled()) {
			throw new Exception("User id: " + receiver.getId() + " is not active. Cannot make borrow request");
		}

		// Check pin from client with matching
		Matching matching = matchingServices.getByPin(
			pin,
			EMatchingStatus.PAIRED.getValue(),
			EMatchingStatus.PENDING.getValue());
		if (matching == null) {
			throw new EntityNotFoundException("Pin: " + pin + " is invalid, could not find any matching with pin");
		}

		// Check returner returns book for himself or not
		Request returnRequest = matching.getReturnerRequest();
		if (returnRequest.getUser().getId().equals(receiver.getId())) {
			throw new Exception("Returner id: " + returnRequest.getId() + " can not return for himself");
		}

		// Check expired time of pin
		long duration = utils.getDuration(matching.getMatchingStartDate(), new Date(), TimeUnit.MINUTES);
		if (duration > Constant.PIN_EXPIRED_MINUTE) {
			// Update returning request + matching status to 'Canceled'
			matching.setStatus(ERequestStatus.CANCELED.getValue());
			matchingServices.updateMatching(matching);

			returnRequest.setStatus(ERequestStatus.CANCELED.getValue());
			requestServices.updateRequest(returnRequest);

			throw new PinExpiredException("Pin is expired");
		}

		// Get book info from matching
		Book book = matching.getBook();
		return new ResponseEntity<>(book, HttpStatus.OK);
	}

	@ApiOperation(value = "Receiver enter pin to get book", response = String.class)
	@PutMapping(value = "/manually/confirm", produces = Constant.APPLICATION_JSON)
	public ResponseEntity<String> confirmBookManually(@RequestBody String body, Principal principal) throws Exception {
		// Check receiver is active or not
		User receiver = userServices.getUserByEmail(principal.getName());
		if (receiver.isDisabled()) {
			return new ResponseEntity<>("User id: " + receiver.getId() + " is not active. Cannot make borrow request", HttpStatus.BAD_REQUEST);
		}

		// Get pin from Request Body
		JSONObject bodyObject = new JSONObject(body);
		String pin = bodyObject.getString("pin");

		// Check pin from client with matching
		Matching matching = matchingServices.getByPin(
			pin,
			EMatchingStatus.PAIRED.getValue(),
			EMatchingStatus.PENDING.getValue());
		if (matching == null) {
			throw new EntityNotFoundException("Pin: " + pin + " is invalid, could not find any matching with pin");
		}

		// Check returner returns book for himself or not
		Request returnRequest = matching.getReturnerRequest();
		if (returnRequest.getUser().getId().equals(receiver.getId())) {
			throw new Exception("Returner id: " + returnRequest.getId() + " can not return for himself");
		}

		// Check expired time of pin
		long duration = utils.getDuration(matching.getMatchingStartDate(), new Date(), TimeUnit.MINUTES);
		if (duration > Constant.PIN_EXPIRED_MINUTE) {
			// Update returning request + matching status to 'Canceled'
			matching.setStatus(EMatchingStatus.CANCELED.getValue());
			matchingServices.updateMatching(matching);

			returnRequest.setStatus(ERequestStatus.CANCELED.getValue());
			requestServices.updateRequest(returnRequest);

			Book book = matching.getBook();
			book.setTransferStatus(EBookTransferStatus.TRANSFERRED.getValue());
			bookServices.updateBook(book);

			throw new PinExpiredException("Pin is expired");
		}

		Book book = matching.getBook();
		// Check book status
		if (book.getStatus().equals(EBookStatus.LOCKED.getValue())) {
			if (!receiver.getRole().getName().equals(Constant.ROLES_LIBRARIAN)) {
				return new ResponseEntity<>("Book status is locked, cannot transfer to other reader", HttpStatus.BAD_REQUEST);
			}
		}

		// Create a borrowing request
		Request borrowingRequest = new Request();
		borrowingRequest.setBook(book);
		borrowingRequest.setUser(receiver);
		borrowingRequest.setStatus(ERequestStatus.COMPLETED.getValue());
		borrowingRequest.setType(ERequestType.BORROWING.getValue());
		borrowingRequest.setMode(ERequestMode.MANUAL.getValue());
		requestServices.saveRequest(borrowingRequest);

		// Update returning request status to 'COMPLETED'
		Request returningRequest = matching.getReturnerRequest();
		returningRequest.setStatus(ERequestStatus.COMPLETED.getValue());
		requestServices.updateRequest(returningRequest);

		//Update matching
		matching.setStatus(EMatchingStatus.CONFIRMED.getValue());
		matching.setBorrowerRequest(borrowingRequest);
		matchingServices.updateMatching(matching);

		// Submit transaction to BC
		JSONObject jsonResult = submitTxToBC(matching, receiver);

		// Check submit transaction to DB success or not
		if (jsonResult.get("status_code").equals(HttpStatus.OK)) {
			jsonResult.remove("status_code");
			return new ResponseEntity<>(jsonResult.toString(), HttpStatus.OK);
		} else {
			jsonResult.remove("status_code");
			return new ResponseEntity<>(jsonResult.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Add transaction to BC, used for MANUAL transfer
	private JSONObject submitTxToBC(Matching matching, User receiver) throws Exception {
		// Init data to submit transaction to BlockChain
		Book book = matching.getBook();
		Request returnRequest = matching.getReturnerRequest();
		Request receiveRequest = matching.getBorrowerRequest();
		User returner = matching.getReturnerRequest().getUser();
		AtomicBoolean callback = new AtomicBoolean(false);
		JSONObject jsonResult = new JSONObject();

		// Set default value for response
		jsonResult.put("message", "failed to submit transaction");
		jsonResult.put("status_code", HttpStatus.INTERNAL_SERVER_ERROR);

		// Update owner of book
		book.setUser(receiver);

		Date sendTime = new Date();

		BookMetadata bookMetadata = book.getMetadata();
		bookMetadata.setStatus(book.getStatus());
		bookMetadata.setTransactionTimestamp(String.valueOf(System.currentTimeMillis() / 1000));

		BigchainTransactionServices services = new BigchainTransactionServices();
		services.doTransfer(
			book.getLastTxId(),
			book.getAssetId(), bookMetadata.getData(),
			returner.getEmail(), receiver.getEmail(),
			(transaction, response) -> { //success

				// Update last transaction id for book
				String transactionId = transaction.getId();
				book.setLastTxId(transactionId);

				// Update transfer status of book
				book.setTransferStatus(EBookTransferStatus.TRANSFERRED.getValue());
				bookServices.updateBook(book);

				// Response to client
				jsonResult.put("message", "confirm book transfer successfully");
				jsonResult.put("status_code", HttpStatus.OK);

				callback.set(true);
			}, (transaction, response) -> { // failed
				// Update request + matching status to 'Canceled'
				matching.setStatus(EMatchingStatus.CANCELED.getValue());
				matchingServices.updateMatching(matching);

				returnRequest.setStatus(ERequestStatus.CANCELED.getValue());
				receiveRequest.setStatus(ERequestStatus.CANCELED.getValue());
				requestServices.updateRequest(returnRequest);
				requestServices.updateRequest(receiveRequest);

				// Update user in book
				book.setUser(returner);
				book.setTransferStatus(EBookTransferStatus.TRANSFERRED.getValue());
				bookServices.updateBook(book);

				notificationService.pushNotification(
					Constant.LIBRARIAN_EMAIL,
					"Transaction chuyển sách của sách có ID là " + book.getId() + " của " + returner.getEmail() + " và " + receiver.getEmail() + " đã thất bại",
					Constant.NOTIFICATION_TYPE_BOOKINSTANCE + ":" + book.getId()
				);

				callback.set(true);
			}
		);

		// Wait until the BigchainTransactionServices has callback or request is timeout
		Date now;
		while (true) {
			now = new Date();
			long duration = utils.getDuration(sendTime, now, TimeUnit.SECONDS);

			if (duration > Constant.BIGCHAIN_REQUEST_TIMEOUT || callback.get()) {
				return jsonResult;
			}
		}
	}

	@ApiOperation(value = "User cancels manually returning request", response = String.class)
	@PutMapping(value = "/cancel", produces = Constant.APPLICATION_JSON)
	public ResponseEntity<String> removeRequest(@RequestBody String body, Principal principal) throws Exception {
		// Get user information
		User sender = userServices.getUserByEmail(principal.getName());

		// Init response to return for client
		JSONObject jsonResult = new JSONObject();

		// Get request
		JSONObject jsonBody = new JSONObject(body);
		Long requestId = jsonBody.getLong("request_id");
		Request request = requestServices.getRequestById(requestId);

		// Check sender is the returner or not
		if (!sender.getId().equals(request.getUser().getId())) {
			throw new Exception("User id: " + sender.getId() + " is not the owner of the request");
		}

		// Check request mode to cancel
		if (request.getMode() == ERequestMode.AUTOMATIC.getValue()) {
			cancelAutoRequest(request);
		} else if (request.getMode() == ERequestMode.MANUAL.getValue()) {
			cancelManualRequest(request);
		} else {
			throw new TypeNotSupportedException("Request mode: " + request.getMode() + " is not supported");
		}

		// Update transfer status of book
		if (request.getType() == ERequestType.RETURNING.getValue()) {
			Book book = request.getBook();
			book.setTransferStatus(EBookTransferStatus.TRANSFERRED.getValue());
			bookServices.updateBook(book);
		}

		//Return response to client
		jsonResult.put("message", "Cancel request successfully");
		return new ResponseEntity<>(jsonResult.toString(), HttpStatus.OK);
	}

	private void cancelAutoRequest(Request request) throws Exception {
		if (request.getStatus() == ERequestStatus.PENDING.getValue()) {
			// Update request status to 'CANCELED'
			request.setStatus(ERequestStatus.CANCELED.getValue());
			requestServices.updateRequest(request);

			// Remove request out of the queue
			requestQueueManager.removeRequestOutTheQueue(request);
		} else if (request.getStatus() == ERequestStatus.MATCHING.getValue()) {
			// Update matching status to 'CANCELED'
			Matching matching;
			Request pairedRequest;
			if (request.getType() == ERequestType.RETURNING.getValue()) {
				matching = matchingServices.getByReturnRequestId(
					request.getId(),
					EMatchingStatus.PAIRED.getValue(),
					EMatchingStatus.PENDING.getValue());

				// Get paired request
				pairedRequest = matching.getBorrowerRequest();
			} else if (request.getType() == ERequestType.BORROWING.getValue()) {
				matching = matchingServices.getByReceiveRequestId(
					request.getId(),
					EMatchingStatus.PAIRED.getValue(),
					EMatchingStatus.PENDING.getValue());

				// Get paired request
				pairedRequest = matching.getReturnerRequest();
			} else {
				throw new Exception("Request with type: " + request.getType() + " is not supported");
			}
			matching.setStatus(EMatchingStatus.CANCELED.getValue());
			matchingServices.updateMatching(matching);

			// Update paired request status from 'MATCHING' to 'PENDING'
			pairedRequest.setStatus(ERequestStatus.PENDING.getValue());
			requestServices.updateRequest(pairedRequest);

			// Find the match for paired request.
			pairRequest(pairedRequest);

			// Update request status to 'CANCELED'
			request.setStatus(ERequestStatus.CANCELED.getValue());
			requestServices.updateRequest(request);
		}
	}

	private void cancelManualRequest(Request request) {
		// Get matching by return request
		Matching matching = matchingServices.getByReturnRequestId(
			request.getId(),
			EMatchingStatus.PAIRED.getValue(),
			EMatchingStatus.PENDING.getValue());
		if (matching != null) {
			// Update matching status to 'CANCELED'
			matching.setStatus(EMatchingStatus.CANCELED.getValue());
			matchingServices.updateMatching(matching);
		}

		// Update request status to 'CANCELED'
		Request returnRequest = Objects.requireNonNull(matching).getReturnerRequest();
		returnRequest.setStatus(ERequestStatus.CANCELED.getValue());
		requestServices.updateRequest(returnRequest);
	}

	@ApiOperation(value = "Receiver rejects to receive book", response = JSONObject.class)
	@PostMapping(value = "/reject", produces = Constant.APPLICATION_JSON)
	public ResponseEntity<String> rejectBook(@RequestBody String body, Principal principal) throws Exception {
		// Get receiver information
		User receiver = userServices.getUserByEmail(principal.getName());

		// Check receiver is the sender
		JSONObject bodyObject = new JSONObject(body);
		String imageUrl = bodyObject.getString("image_url");
		Long matchingId = bodyObject.getLong("matching_id");
		String reason = bodyObject.getString("reason");

		Matching matching = matchingServices.getMatchingById(matchingId);
		if (matching == null) {
			return new ResponseEntity<>("Matching id: " + matchingId + " not found", HttpStatus.BAD_REQUEST);
		}
		if (!matching.getBorrowerRequest().getUser().getId().equals(receiver.getId())) {
			return new ResponseEntity<>("User id: " + receiver.getId() + " is not the receiver", HttpStatus.BAD_REQUEST);
		}
		if (matching.getStatus() != EMatchingStatus.PENDING.getValue()) {
			return new ResponseEntity<>("Cannot reject matching with status: " + matching.getStatus(), HttpStatus.BAD_REQUEST);
		}

		// Get hash value
		InputStreamResource resource = utils.downloadFileTos3bucket(imageUrl);
		String hashValue = ImageHelper.hashFromUrl(resource);

		// Init data so submit transaction to BC
		Book book = matching.getBook();
		Request returnRequest = matching.getReturnerRequest();
		Request receiveRequest = matching.getBorrowerRequest();
		User returner = returnRequest.getUser();
		Date sendTime = new Date();
		AtomicBoolean callback = new AtomicBoolean(false);
		JSONObject jsonResult = new JSONObject();

		// Set default value for response
		jsonResult.put("message", "failed to submit transaction");
		jsonResult.put("status_code", HttpStatus.INTERNAL_SERVER_ERROR);

		bookServices.getLastTransactionFromBigchain(book);
		BookMetadata bookMetadata = book.getMetadata();

		bookMetadata.setRejectReason(reason);
		bookMetadata.setImgHash(hashValue);
		bookMetadata.setImageLink(imageUrl);
		bookMetadata.setRejectorEmail(receiver.getEmail());
		// Update reject count
		bookMetadata.increaseLastRejectCount();

		// Update book status
		if (bookMetadata.isLastRejectCountOver()) {
			bookMetadata.setStatus(EBookStatus.LOCKED.getValue());
		} else {
			bookMetadata.setStatus(book.getStatus());
		}

		// Set new transaction timestamp
		bookMetadata.setTransactionTimestamp(String.valueOf(System.currentTimeMillis() / 1000));

		// Submit transaction to BC
		BigchainTransactionServices services = new BigchainTransactionServices();
		services.doTransfer(
			book.getLastTxId(),
			book.getAssetId(), bookMetadata.getData(),
			returner.getEmail(), returner.getEmail(),
			(transaction, response) -> { // success

				// Update last transaction id for book
				String transactionId = transaction.getId();
				book.setLastTxId(transactionId);

				// Update matching status to 'Confirmed'
				matching.setStatus(EMatchingStatus.REJECTED.getValue());
				matchingServices.updateMatching(matching);

				// Update returner request status to 'Completed'
				returnRequest.setStatus(ERequestStatus.COMPLETED.getValue());
				requestServices.updateRequest(returnRequest);

				// Push notification to returner
				notificationService.pushNotification(
					returnRequest.getUser().getEmail(),
					"Người mượn đã từ chối nhận sách " + book.getBookDetail().getName(),
					Constant.NOTIFICATION_TYPE_KEEPING
				);

				// Put borrow request in queue after rejecting
				try {
					receiveRequest.setStatus(ERequestStatus.PENDING.getValue());
					receiveRequest.setPairedUser(null);
					pairRequest(receiveRequest);
				} catch (NotFoundException ex) {
					receiveRequest.setStatus(ERequestStatus.COMPLETED.getValue());
					requestServices.updateRequest(receiveRequest);
				}

				// Update transfer status of book
				book.setTransferStatus(EBookTransferStatus.TRANSFERRED.getValue());
				bookServices.updateBook(book);

				if (bookMetadata.isLastRejectCountOver()) { // If reject count > 5
					// Update book status
					book.setStatus(EBookStatus.LOCKED.getValue());
					bookServices.updateBook(book);

					// Push notification to librarian
					notificationService.pushNotification(
						Constant.LIBRARIAN_EMAIL,
						"Sách " + book.getBookDetail().getName() + "- #" + book.getId() + " đã bị từ chối " +
							bookMetadata.getRejectCount() + " lần.",
						Constant.NOTIFICATION_TYPE_BOOKINSTANCE + ":" + book.getId()
					);
				}

				// Override value in response
				jsonResult.put("message", "reject to transfer book successfully");
				jsonResult.put("status_code", HttpStatus.OK);

				callback.set(true);
			},
			(transaction, response) -> { // failed
				// Update request + matching status to 'Canceled'
				matching.setStatus(EMatchingStatus.CANCELED.getValue());
				matchingServices.updateMatching(matching);

				returnRequest.setStatus(ERequestStatus.CANCELED.getValue());
				receiveRequest.setStatus(ERequestStatus.CANCELED.getValue());
				requestServices.updateRequest(returnRequest);
				requestServices.updateRequest(receiveRequest);
				// Update user in book
				book.setUser(returner);
				book.setTransferStatus(EBookTransferStatus.TRANSFERRED.getValue());
				bookServices.updateBook(book);

				// Push notification to librarian
				notificationService.pushNotification(
					Constant.LIBRARIAN_EMAIL,
					"Transaction từ chối của sách có ID là " + book.getId() + " của " + returner.getEmail() + " và " + receiver.getEmail() + " đã thất bại",
					Constant.NOTIFICATION_TYPE_BOOKINSTANCE + ":" + book.getId()
				);

				callback.set(true);
			}
		);

		// Wait until the BigchainTransactionServices has callback or request is timeout
		Date now;
		while (true) {
			now = new Date();
			long duration = utils.getDuration(sendTime, now, TimeUnit.SECONDS);

			if (duration > Constant.BIGCHAIN_REQUEST_TIMEOUT || callback.get()) {
				return new ResponseEntity<>(jsonResult.toString(), HttpStatus.OK);
			}
		}
	}

	@Autowired
	AmazonS3 s3Client;

	@ApiOperation(value = "Create a transaction", response = Request.class)
	@PostMapping("/upload")
	public ResponseEntity<String> testUploadFile(@RequestParam("file") MultipartFile file) {
		String fileUrl = utils.uploadFile(file);
		JSONObject responseObj = new JSONObject();
		responseObj.put("url", fileUrl);
		return new ResponseEntity<>(responseObj.toString(), HttpStatus.CREATED);
	}

	@ApiOperation(value = "Get Request Overview", response = String.class)
	@RequestMapping(value = "/overview", method = RequestMethod.GET, produces = Constant.APPLICATION_JSON)
	public ResponseEntity<String> getQueueInfo() throws JsonProcessingException {
		long pedingRequest = requestServices.countRequestByStatus(ERequestStatus.PENDING.getValue());
		long matchingRequest = requestServices.countRequestByStatus(ERequestStatus.MATCHING.getValue());
		JSONObject result = new JSONObject();
		result.put("totalPendingRequest", pedingRequest);
		result.put("totalMatchingRequest", matchingRequest);
		return new ResponseEntity<>(result.toString(), HttpStatus.OK);
	}

}
