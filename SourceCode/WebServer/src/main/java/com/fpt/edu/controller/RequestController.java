package com.fpt.edu.controller;

import com.fpt.edu.common.ERequestStatus;
import com.fpt.edu.common.ETransferType;
import com.fpt.edu.common.EMatchingStatus;
import com.fpt.edu.common.ERequestType;
import com.fpt.edu.common.RequestQueueSimulate.Message;
import com.fpt.edu.common.RequestQueueSimulate.PublishSubscribe;
import com.fpt.edu.common.RequestQueueSimulate.RequestQueueManager;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.*;
import com.fpt.edu.exception.*;
import com.fpt.edu.services.*;
import io.swagger.annotations.ApiOperation;
import org.hibernate.Hibernate;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("requests")
public class RequestController extends BaseController {
	private final RequestServices requestServices;
	private final UserServices userServices;
	private final BookDetailsServices bookDetailsServices;
	private final BookServices bookServices;
	private final MatchingServices matchingServices;
	private final TransactionServices transactionServices;
	private final PublishSubscribe publishSubscribe;
	private final RequestQueueManager requestQueueManager;

	@Autowired
	public RequestController(RequestServices requestServices, UserServices userServices,
							 BookDetailsServices bookDetailsServices, BookServices bookServices,
							 MatchingServices matchingServices, TransactionServices transactionServices,
							 PublishSubscribe publishSubscribe, RequestQueueManager requestQueueManager) {
		this.requestServices = requestServices;
		this.userServices = userServices;
		this.bookDetailsServices = bookDetailsServices;
		this.bookServices = bookServices;
		this.matchingServices = matchingServices;
		this.transactionServices = transactionServices;
		this.publishSubscribe = publishSubscribe;
		this.requestQueueManager = requestQueueManager;
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
	public ResponseEntity<List<Request>> getBookRequestList(@RequestParam int type) {
		// Get user information
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = (String) authentication.getPrincipal();
		User user = userServices.getUserByEmail(email);

		// Get book list of user, by default lazy loading is enable
		Hibernate.initialize(user.getListBooks());

		// Get all requests based on user id and request type, with status not completed
		List<Request> requestList = requestServices.findByUserIdAndType(user.getId(), type, ERequestStatus.COMPLETED.getValue());

		// Get paired user information from the request whose status is matching
		for (Request r : requestList) {
			if (r.getStatus() == ERequestStatus.MATCHING.getValue()) {
				if (r.getType() == ERequestType.RETURNING.getValue()) {

					// Get matching based on request id, matching status has not confirmed yet
					Matching matching = matchingServices.getByReturnRequestId(r.getId(), EMatchingStatus.CONFIRMED.getValue());

					// Set paired user information for request
					User borrower = matching.getBorrowerRequest().getUser();
					r.setPairedUser(borrower);
				} else if (r.getType() == ERequestType.BORROWING.getValue()) {

					// Get matching based on request id, matching status has not confirmed yet
					Matching matching = matchingServices.getByReceiveRequestId(r.getId(), EMatchingStatus.CONFIRMED.getValue());

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
	public ResponseEntity<String> requestBook(@RequestBody String body) throws EntityNotFoundException,
		TypeNotSupportedException, EntityAldreayExisted, NotFoundException {
		// Get user information
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = (String) authentication.getPrincipal();
		User user = userServices.getUserByEmail(email);

		//Get type from Request Body
		JSONObject bodyObject = new JSONObject(body);
		int type = bodyObject.getInt("type");

		// Init a request to insert to DB
		Request request;

		// Fill data for request
		if (type == ERequestType.BORROWING.getValue()) {
			// Get book name from Request Body
			String bookName = bodyObject.getString("book_name");

			request = getBorrowingRequest(type, user, bookName);
		} else if (type == ERequestType.RETURNING.getValue()) {
			// Get book id from Request Body
			Long bookId = bodyObject.getLong("book_id");

			request = getReturningRequest(type, user, bookId);
		} else {
			throw new TypeNotSupportedException("Type " + type + " is not supported");
		}

		// Find the matching request
		pairRequest(request);

//        requestServices.saveRequest(request);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("message", "success");
		return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
	}

	private Request getBorrowingRequest(int type, User user, String bookName)
		throws EntityNotFoundException, EntityAldreayExisted {
		// Get book detail object from DB
		BookDetail bookDetail = bookDetailsServices.getBookDetailByName(bookName);
		if (bookDetail == null) {
			throw new EntityNotFoundException("Book name: " + bookName + " not found");
		}

		//check existed request based on request type, user id, book detail, with request status is not completed
		boolean existed = requestServices.checkExistedRequest(type, user.getId(), ERequestStatus.COMPLETED.getValue(),
			bookDetail.getId(), (long) 0);
		if (existed) {
			throw new EntityAldreayExisted("Request's already existed");
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
		//if user's not keeping this book but want to return book, throw exception
		if (!keeping) {
			throw new EntityNotFoundException("This book is not found in the current book list");
		}

		//check request existed based on request type, user id, book id, with request status is not completed
		boolean existed = requestServices.checkExistedRequest(type, user.getId(), ERequestStatus.COMPLETED.getValue(),
			(long) 0, book.getId());
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
			//remove reqyuest
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
			matching.setStatus(EMatchingStatus.PENDING.getValue());

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
		}
	}

	@ApiOperation(value = "Transfer book for returner and receiver", response = String.class)
	@PutMapping(value = "/transfer", produces = Constant.APPLICATION_JSON)
	public ResponseEntity<String> transferBook(@RequestBody String body) throws Exception {
		// Get user information
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = (String) authentication.getPrincipal();
		User sender = userServices.getUserByEmail(email);

		// Get data from Request Body
		JSONObject bodyObject = new JSONObject(body);
		int type = bodyObject.getInt("type");
		Long matchingId = bodyObject.getLong("matchingId");

		// Init JSONObject to return response
		JSONObject jsonResult;

		// Check whether matchingId is existed or not
		Matching matching = matchingServices.getMatchingById(matchingId);
		if (matching == null) {
			throw new EntityNotFoundException("Matching id: " + matchingId + " not found");
		}

		// Check transfer type is returner or receiver
		if (type == ETransferType.RETURNER.getValue()) {
			jsonResult = returnBook(matching, sender);
		} else if (type == ETransferType.RECEIVER.getValue()) {
			String pin = bodyObject.getString("pin");
			jsonResult = receiveBook(matching, sender, pin);
		} else {
			throw new TypeNotSupportedException("Type: " + type + " is not supported");
		}
		return new ResponseEntity<>(jsonResult.toString(), HttpStatus.OK);
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

	private JSONObject addTransferTxToBC(Matching matching) throws Exception {
		// Init data to submit transaction to BlockChain
		Book book = bookServices.getBookById(matching.getBook().getId());
		Request returnerRequest = matching.getReturnerRequest();
		Request receiverRequest = matching.getBorrowerRequest();
		User returner = returnerRequest.getUser();
		User receiver = receiverRequest.getUser();
		Date sendTime = new Date();
		AtomicBoolean callback = new AtomicBoolean(false);
		JSONObject jsonResult;
		// Update current_keeper
		book.setUser(receiver);

		// Submit transaction to BlockChain
		BigchainTransactionServices services = new BigchainTransactionServices();
		services.doTransfer(
			book.getLastTxId(),
			book.getAssetId(), book.getMetadata(),
			String.valueOf(returner.getEmail()), String.valueOf(receiver.getEmail()),
			(transaction, response) -> { // Success

				// Update last transaction id for book
				String tracsactionId = transaction.getId();
				book.setLastTxId(tracsactionId);

				// Update status of request to "completed"
				returnerRequest.setStatus(ERequestStatus.COMPLETED.getValue());
				receiverRequest.setStatus(ERequestStatus.COMPLETED.getValue());
				requestServices.updateRequest(returnerRequest);
				requestServices.updateRequest(receiverRequest);

				//update status of matching
				matching.setStatus(EMatchingStatus.CONFIRMED.getValue());
				matchingServices.updateMatching(matching);

				// Update book
				bookServices.updateBook(book);

				//insert a transaction to DB Postgresql
				Transaction tran = new Transaction();
				tran.setBook(book);
				tran.setReturner(returner);
				tran.setBorrower(receiver);
				transactionServices.insertTransaction(tran);

				callback.set(true);
			},
			(transaction, response) -> { //failed
				callback.set(true);
			}
		);

		// Wait until the BigchainTransactionServices has callback or request is timeout
		Date now;
		while (true) {
			now = new Date();
			long duration = utils.getDuration(sendTime, now, TimeUnit.SECONDS);

			if (duration > 30 || callback.get()) {
				jsonResult = new JSONObject();
				jsonResult.put("message", "confirm book transfer successfully");
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
		Matching matched = matchingServices.getByRequestId(id);
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
		Matching existedMatching = matchingServices.getByBookId(bookId, EMatchingStatus.CONFIRMED.getValue());
		if (existedMatching != null) {
			long duration = utils.getDuration(existedMatching.getMatchingStartDate(), now, TimeUnit.MINUTES);

			//if pin is expired
			if (duration > Constant.PIN_EXPIRED_MINUTE) {
				// Update matching
				String pin = utils.getPin();
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
		returningRequest.setStatus(ERequestStatus.PENDING.getValue());
		returningRequest.setType(ERequestType.RETURNING.getValue());
		requestServices.saveRequest(returningRequest);

		// Check whether pin is duplicated or not
		String pin;
		Matching duplicatedMat;
		do {
			pin = utils.getPin();
			duplicatedMat = matchingServices.getByPin(pin, EMatchingStatus.CONFIRMED.getValue());
		} while (duplicatedMat != null);

		// Init matching instance
		Matching matching = new Matching();
		matching.setReturnerRequest(returningRequest);
		matching.setBook(book);
		matching.setPin(pin);
		matching.setMatchingStartDate(now);
		matching.setStatus(EMatchingStatus.PENDING.getValue());
		matchingServices.saveMatching(matching);

		// Get matching id
		Matching m = matchingServices.getByBookId(book.getId(), EMatchingStatus.CONFIRMED.getValue());
		Long matchingId = m.getId();

		// Return response to client
		jsonResult.put("pin", pin);
		jsonResult.put("matching_id", matchingId);
		jsonResult.put("created_at", now.getTime());
		return new ResponseEntity<>(jsonResult.toString(), HttpStatus.OK);
	}

	@ApiOperation(value = "Receiver enter pin to get book", response = String.class)
	@PutMapping(value = "/manually/verify", produces = Constant.APPLICATION_JSON)
	public ResponseEntity<String> verifyBookManually(@RequestBody String body, Principal principal) throws Exception {
		// Get user information
		User receiver = userServices.getUserByEmail(principal.getName());

		// Get pin from Request Body
		JSONObject bodyObject = new JSONObject(body);
		String pin = bodyObject.getString("pin");

		// Check pin from client with matching
		Matching matching = matchingServices.getByPin(pin, EMatchingStatus.CONFIRMED.getValue());
		if (matching == null) {
			throw new EntityNotFoundException("Pin: " + pin + " is invalid, could not find any matching with pin");
		}

		// Check returner returns book for himself or not
		if (matching.getReturnerRequest().getUser().getId().equals(receiver.getId())) {
			throw new Exception("Returner id: " + matching.getReturnerRequest().getId() + " can not return for himself");
		}

		// Check expired time of pin
		long duration = utils.getDuration(matching.getMatchingStartDate(), new Date(), TimeUnit.MINUTES);
		if (duration > Constant.PIN_EXPIRED_MINUTE) {
			// Delete returning request + matching
			deleteRequestAndMatching(matching);
			throw new PinExpiredException("Pin is expired");
		}

		// Create a borrowing request
		Request borrowingRequest = new Request();
		borrowingRequest.setBook(matching.getBook());
		borrowingRequest.setUser(receiver);
		borrowingRequest.setStatus(ERequestStatus.COMPLETED.getValue());
		borrowingRequest.setType(ERequestType.BORROWING.getValue());
		requestServices.saveRequest(borrowingRequest);

		// Update returning request status to 'COMPLETED'
		Request returningRequest = matching.getReturnerRequest();
		returningRequest.setStatus(ERequestStatus.COMPLETED.getValue());
		requestServices.updateRequest(returningRequest);

		//Update matching
		matching.setStatus(EMatchingStatus.CONFIRMED.getValue());
		matching.setBorrowerRequest(borrowingRequest);
		matchingServices.updateMatching(matching);

		// Init data to submit transaction to BlockChain
		Book book = matching.getBook();
		User returner = matching.getReturnerRequest().getUser();
		AtomicBoolean callback = new AtomicBoolean(false);
		JSONObject jsonResult;

		// Update owner of book
		book.setUser(receiver);

		Date sendTime = new Date();

		BigchainTransactionServices services = new BigchainTransactionServices();
		services.doTransfer(
			book.getLastTxId(),
			book.getAssetId(), book.getMetadata(),
			returner.getEmail(), receiver.getEmail(),
			(transaction, response) -> { //success

				// Update last transaction id for book
				String transactionId = transaction.getId();
				book.setLastTxId(transactionId);

				// Update book
				bookServices.updateBook(book);

				// Insert a transaction to DB Postgresql
				Transaction tran = new Transaction();
				tran.setBook(book);
				tran.setReturner(returner);
				tran.setBorrower(receiver);
				transactionServices.insertTransaction(tran);

				callback.set(true);
			}, (transaction, response) -> { // failed
				callback.set(true);

				// Delete request + matching
				deleteRequestAndMatching(matching);

				// Update user in book
				book.setUser(returner);
				bookServices.updateBook(book);
			}
		);

		// Wait until the BigchainTransactionServices has callback or request is timeout
		Date now;
		while (true) {
			now = new Date();
			duration = utils.getDuration(sendTime, now, TimeUnit.SECONDS);

			if (duration > 30 || callback.get()) {
				jsonResult = new JSONObject();
				jsonResult.put("message", "confirm book transfer successfully");
				return new ResponseEntity<>(jsonResult.toString(), HttpStatus.OK);
			}
		}
	}

	private void deleteRequestAndMatching(Matching matching) {
		matchingServices.deleteMatching(matching.getId());
		requestServices.deleteRequest(matching.getReturnerRequest().getId());
	}

	@ApiOperation(value = "User cancels manually returning request", response = String.class)
	@PutMapping(value = "/manually/cancel", produces = Constant.APPLICATION_JSON)
	public ResponseEntity<String> removeRequestManually(@RequestBody String body, Principal principal) throws Exception {
		// Get user information
		User sender = userServices.getUserByEmail(principal.getName());

		// Get request
		JSONObject jsonBody = new JSONObject(body);
		Long requestId = jsonBody.getLong("request_id");
		Request request = requestServices.getRequestById(requestId);

		// Check sender is the returner or not
		if (!sender.getId().equals(request.getUser().getId())) {
			throw new Exception("User id: " + sender + " is not the returner of the request");
		}
		// Get matching by return request
		Matching matching = matchingServices.getByReturnRequestId(requestId, EMatchingStatus.CONFIRMED.getValue());

		// Update matching status to 'CANCELED'
		matching.setStatus(EMatchingStatus.CANCELED.getValue());
		matchingServices.updateMatching(matching);

		// Update request status to 'CANCELED'
		Request returnRequest = matching.getReturnerRequest();
		returnRequest.setStatus(ERequestStatus.CANCELED.getValue());
		requestServices.updateRequest(returnRequest);

		JSONObject jsonResult = new JSONObject();
		jsonResult.put("message", "Canceled request sucessfully");

		return new ResponseEntity<>(jsonResult.toString(), HttpStatus.OK);
	}
}
