package com.fpt.edu.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.edu.common.enums.*;
import com.fpt.edu.common.helpers.ImportHelper;
import com.fpt.edu.common.request_queue_simulate.RequestQueue;
import com.fpt.edu.services.NotificationService;
import com.fpt.edu.common.helpers.SchedulerJob;
import com.fpt.edu.common.request_queue_simulate.PublishSubscribe;
import com.fpt.edu.common.request_queue_simulate.RequestQueueManager;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.*;
import com.fpt.edu.exceptions.EntityNotFoundException;
import com.fpt.edu.exceptions.InvalidExpressionException;
import com.fpt.edu.services.*;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.iterators.EntrySetMapIterator;
import org.aspectj.util.FileUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("librarian")
public class LibrarianController extends BaseController {

	private String cronExpression = "";

	@Autowired
	private NotificationService notificationService;

	public LibrarianController(UserServices userServices, BookDetailsServices bookDetailsServices, BookServices bookServices, ImportHelper importHelper, MatchingServices matchingServices, RequestServices requestServices, PublishSubscribe publishSubscribe, RequestQueueManager requestQueueManager, NotificationService notificationService) {
		super(userServices, bookDetailsServices, bookServices, importHelper, matchingServices, requestServices, publishSubscribe, requestQueueManager, notificationService);
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
		userServices.getByUserId(id);
		List<Book> bookList = userServices.getCurrentBookListOfUser(id);
		return new ResponseEntity<>(bookList, HttpStatus.OK);
	}

	@ApiOperation(value = "Disable a user", response = User.class)
	@PutMapping("/users/{id}")
	public ResponseEntity<User> disableUser(@PathVariable Long id,
											@RequestParam("disable") boolean disable,
											Principal principal) {
		// Check sender is librarian or not
		User librarian = userServices.getUserByEmail(principal.getName());
		if (!librarian.getRole().getName().equals(Constant.ROLES_LIBRARIAN)) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

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
			bookDetailList = bookDetailsServices.searchBookDetails(name.toLowerCase(), pageable);
		} else {
			bookDetailList = bookDetailsServices.getAllBookDetails(pageable);
		}

		JSONArray arrResult = new JSONArray();
		for (BookDetail bookDetail : bookDetailList) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(Constant.ID, bookDetail.getId());
			jsonObject.put(Constant.NAME, bookDetail.getName());
			jsonObject.put(Constant.IMAGE, bookDetail.getThumbnail());
			jsonObject.put(Constant.SUBJECT_CODE, bookDetail.getParseedSubjectCode());
			jsonObject.put(Constant.PREVIEW_LINK, bookDetail.getPreviewLink());
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

	@ApiOperation(value = "Import book from file", response = Book.class)
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
	public ResponseEntity<String> transferBook(@RequestParam Long book_detail_id, Principal principal)
		throws EntityNotFoundException {

		// Check sender is librarian
		User librarian = userServices.getUserByEmail(principal.getName());
		if (!librarian.getRole().getName().equals(Constant.ROLES_LIBRARIAN)) {
			return new ResponseEntity<>("Sender is not librarian role", HttpStatus.BAD_REQUEST);
		}

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
		returningRequest.setStatus(ERequestStatus.MATCHING.getValue());
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

	@ApiOperation(value = "Get overview of books", response = Book.class)
	@GetMapping("/overviews")
	public ResponseEntity<String> getOvervide() throws Exception {
		long totalBookDetail = bookDetailsServices.countNumberOfBookDetail();
		long totalBookInstance = bookServices.countNumberOfBookDetails();
		long totalUser = userServices.countTotalUser();
		JSONObject result = new JSONObject();
		result.put("totalBookDetails", totalBookDetail);
		result.put("totalBookInstances", totalBookInstance);
		result.put("totalUsers", totalUser);
		return new ResponseEntity<>(result.toString(), HttpStatus.OK);
	}

	@ApiOperation(value = "Enable or disable system scheduler", response = String.class)
	@PutMapping("/scheduler/enable")
	public ResponseEntity<String> enableScheduler(
		@RequestBody String body,
		Principal principal
	) throws InvalidExpressionException, SchedulerException {
		// Check sender is librarian or not
		User librarian = userServices.getUserByEmail(principal.getName());
		if (!librarian.getRole().getName().equals(Constant.ROLES_LIBRARIAN)) {
			return new ResponseEntity<>("The sender is not librarian role", HttpStatus.BAD_REQUEST);
		}

		// Get data from request body
		JSONObject bodyObject = new JSONObject(body);
		boolean enable = bodyObject.getBoolean("enable");
		cronExpression = bodyObject.getString("interval_expression");

		// Get scheduler
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

		if (!enable) {
			scheduler.shutdown();
			LOGGER.info("Shut down scheduler");
			return new ResponseEntity<>("Shut down scheduler successfully", HttpStatus.OK);
		}

		if (!CronExpression.isValidExpression(cronExpression)) {
			throw new InvalidExpressionException(cronExpression + " expression is invalid");
		}

		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("RequestServices", requestServices);
		jobDataMap.put("MatchingServices", matchingServices);
		jobDataMap.put("NotificationHelper", notificationService);

		JobDetail jobDetail = JobBuilder.newJob(SchedulerJob.class).setJobData(jobDataMap).build();

		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("CronTrigger")
			.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();

		scheduler.scheduleJob(jobDetail, trigger);
		scheduler.start();
		LOGGER.info("Started scheduler");

		return new ResponseEntity<>("Started Scheduler Successfully", HttpStatus.OK);
	}

	@ApiOperation(value = "Get scheduler status", response = JSONObject.class)
	@GetMapping("/scheduler/status")
	public ResponseEntity<String> getSchedulerStatus(Principal principal) throws SchedulerException {
		// Check sender is librarian or not
		User librarian = userServices.getUserByEmail(principal.getName());
		if (!librarian.getRole().getName().equals(Constant.ROLES_LIBRARIAN)) {
			return new ResponseEntity<>("The sender is not librarian role", HttpStatus.BAD_REQUEST);
		}

		JSONObject jsonResult = new JSONObject();

		// Get scheduler
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		if (!scheduler.isStarted() || scheduler.isShutdown()) {
			jsonResult.put("enable", Boolean.FALSE);
		} else if (scheduler.isStarted()) {
			jsonResult.put("enable", Boolean.TRUE);
			jsonResult.put("interval_expression", cronExpression);
		}

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

	@ApiOperation(value = "Sync current keeper of a book instance back from bigchain", response = String.class)
	@PutMapping("/books/{book_id}/sync_current_keeper")
	public ResponseEntity<String> syncCurrentKeeperFromBigchain(
		@PathVariable("book_id") Long bookId
	) throws Exception {
		Book book = bookServices.getBookById(bookId);
		bookServices.getLastTransactionFromBigchain(book);
		User currentKeeper = userServices.getUserByEmail(book.getMetadata().getCurrentKeeper());
		book.setUser(currentKeeper);
		bookServices.updateBook(book);

		JSONObject response = new JSONObject();
		response.put("bookId", bookId);
		response.put("currentKeeper", book.getUser().getEmail());

		return new ResponseEntity<>(response.toString(), HttpStatus.OK);
	}

	@ApiOperation(value = "Send notification", response = String.class)
	@PostMapping("/notification")
	public ResponseEntity<String> pushNotification(@RequestBody String body, Principal principal) throws IOException, UnirestException {
		User librarian = userServices.getUserByEmail(principal.getName());
		if (!librarian.getRole().getName().equals(Constant.ROLES_LIBRARIAN)) {
			return new ResponseEntity<>("The sender is not librarian role", HttpStatus.BAD_REQUEST);
		}

		JSONObject bodyObject = new JSONObject(body);
		String email = bodyObject.getString("email");
		String message = bodyObject.getString("message");
		String type = bodyObject.getString("type");

		notificationService.pushNotification(email, message, type);

		return new ResponseEntity<>("Push notification successfully", HttpStatus.OK);
	}

	@ApiOperation(value = "Get book instance total of book detail at library", response = String.class)
	@GetMapping("/book_total")
	public ResponseEntity<String> getLibrayBookTotal(@RequestParam Long book_detail_id, Principal principal) {
		// Check sender is librarian or not
		User librarian = userServices.getUserByEmail(principal.getName());
		if (!librarian.getRole().getName().equals(Constant.ROLES_LIBRARIAN)) {
			return new ResponseEntity<>("The sender is not librarian role", HttpStatus.BAD_REQUEST);
		}

		long total = bookServices.getBookTotalAtLibrary(book_detail_id, librarian.getId());

		JSONObject jsonResult = new JSONObject();
		jsonResult.put("total", total);

		return new ResponseEntity<>(jsonResult.toString(), HttpStatus.OK);
	}

	@ApiOperation(value = "Librarian ends life cycle of book", response = JSONObject.class)
	@PutMapping("/end_book")
	public ResponseEntity<String> endBookLife(@RequestBody String body, Principal principal) throws Exception {
		JSONObject jsonResult = new JSONObject();
		AtomicBoolean callback = new AtomicBoolean(false);
		Date sendTime = new Date();

		// Check sender is librarian or not
		User librarian = userServices.getUserByEmail(principal.getName());
		if (!librarian.getRole().getName().equals(Constant.ROLES_LIBRARIAN)) {
			jsonResult.put("message", "The sender is not librarian role");
			return new ResponseEntity<>(jsonResult.toString(), HttpStatus.BAD_REQUEST);
		}

		JSONObject bodyObject = new JSONObject(body);
		Long bookId = bodyObject.getLong("book_id");

		Book book = bookServices.getBookById(bookId);

		if (!book.getStatus().equals(EBookStatus.LOCKED.getValue())) {
			jsonResult.put("message", "This book is in 'locked' status");
			return new ResponseEntity<>(jsonResult.toString(), HttpStatus.BAD_REQUEST);
		}

		BookMetadata bookMetadata = book.getMetadata();
		bookMetadata.setCurrentKeeper(Constant.EMPTY_VALUE);
		bookMetadata.setStatus(EBookStatus.DAMAGED.getValue());
		bookMetadata.setTransactionTimestamp(String.valueOf(System.currentTimeMillis() / 1000));

		// Set default value for response
		jsonResult.put("message", "failed to submit transaction");
		jsonResult.put("status_code", HttpStatus.INTERNAL_SERVER_ERROR.value());

		BigchainTransactionServices services = new BigchainTransactionServices();
		services.doTransfer(
			book.getLastTxId(),
			book.getAssetId(), bookMetadata.getData(),
			book.getUser().getEmail(), Constant.EMPTY_VALUE,
			(transaction, response) -> { // success

				String transactionId = transaction.getId();
				book.setLastTxId(transactionId);

				// Update in DB
				book.setUser(null);
				book.setStatus(EBookStatus.DAMAGED.getValue());
				bookServices.updateBook(book);

				jsonResult.put("message", "End book lifecycle successfully");
				jsonResult.put("status_code", HttpStatus.OK.value());

				callback.set(true);
			},
			(transaction, response) -> { // failed
				callback.set(true);
			}
		);

		// Wait until the BigChain has callback or request timeout
		Date now;
		while (true) {
			now = new Date();
			long duration = utils.getDuration(sendTime, now, TimeUnit.SECONDS);
			if (duration > Constant.BIGCHAIN_REQUEST_TIMEOUT || callback.get()) {
				if (jsonResult.get("status_code").equals(HttpStatus.OK.value())) {
					return new ResponseEntity<>(jsonResult.toString(), HttpStatus.OK);
				} else {
					return new ResponseEntity<>(jsonResult.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		}
	}

	@ApiOperation(value = "Librarian reuse book", response = JSONObject.class)
	@PutMapping("/reuse_book")
	public ResponseEntity<JSONObject> reuseBook(@RequestBody String body, Principal principal) throws Exception {
		// Init data
		JSONObject jsonResult = new JSONObject();
		AtomicBoolean callback = new AtomicBoolean(false);
		Date sendTime = new Date();

		// Check sender is librarian or not
		User librarian = userServices.getUserByEmail(principal.getName());
		if (!librarian.getRole().getName().equals(Constant.ROLES_LIBRARIAN)) {
			jsonResult.put("message", "The sender is not librarian role");
			return new ResponseEntity<>(jsonResult, HttpStatus.BAD_REQUEST);
		}

		// Get book
		JSONObject bodyObject = new JSONObject(body);
		Long bookId = bodyObject.getLong("book_id");
		Book book = bookServices.getBookById(bookId);

		if (!book.getStatus().equals(EBookStatus.LOCKED.getValue())) {
			jsonResult.put("message", "This book is in 'locked' status");
			return new ResponseEntity<>(jsonResult, HttpStatus.BAD_REQUEST);
		}

		BookMetadata bookMetadata = book.getMetadata();
		bookMetadata.resetRejectCount();
		bookMetadata.setStatus(EBookStatus.IN_USE.getValue());
		bookMetadata.setTransactionTimestamp(String.valueOf(System.currentTimeMillis() / 1000));

		// Set default value for response
		jsonResult.put("message", "failed to submit transaction");
		jsonResult.put("status_code", HttpStatus.INTERNAL_SERVER_ERROR.value());

		BigchainTransactionServices services = new BigchainTransactionServices();
		services.doTransfer(
			book.getLastTxId(),
			book.getAssetId(), bookMetadata.getData(),
			book.getUser().getEmail(), librarian.getEmail(),
			(transaction, response) -> { // success

				String transactionId = transaction.getId();
				book.setLastTxId(transactionId);
				book.setStatus(EBookStatus.IN_USE.getValue());
				bookServices.updateBook(book);

				jsonResult.put("message", "Submit transaction successfully");
				jsonResult.put("status_code", HttpStatus.OK.value());

				callback.set(true);
			},
			(transaction, response) -> { // failed

				callback.set(true);
			}
		);

		// Wait until Bigchain has callback or request time out
		Date now;
		while (true) {
			now = new Date();
			long duration = utils.getDuration(sendTime, now, TimeUnit.SECONDS);

			if (duration > Constant.BIGCHAIN_REQUEST_TIMEOUT || callback.get()) {
				if (jsonResult.get("status_code").equals(HttpStatus.OK.value())) {
					return new ResponseEntity<>(jsonResult, HttpStatus.OK);
				} else {
					return new ResponseEntity<>(jsonResult, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		}
	}


	@ApiOperation(value = "Get Queue Overview", response = String.class)
	@RequestMapping(value = "/queue/overview", method = RequestMethod.GET, produces = Constant.APPLICATION_JSON)
	public ResponseEntity<String> getQueueInfo() throws JsonProcessingException {
		HashMap<Long, RequestQueue> requestQueueMap = (HashMap<Long, RequestQueue>) this.requestQueueManager.getRequestMap();
		JSONArray response = new JSONArray();
		Iterator<Map.Entry<Long, RequestQueue>> iterator = requestQueueMap.entrySet().iterator();
		JSONArray result = new JSONArray();
		ObjectMapper mapper = new ObjectMapper();
		while (iterator.hasNext()) {
			JSONObject bookQueueNode = new JSONObject();
			Map.Entry<Long, RequestQueue> currentBookQueue = iterator.next();
			BookDetail bookDetail = bookDetailsServices.getBookById(currentBookQueue.getKey());
			bookQueueNode.put("bookDetail", new JSONObject(mapper.writeValueAsString(bookDetail)));
			PriorityQueue<Request> borrowQueue = currentBookQueue.getValue().getBorrowRequestQueue();
			JSONArray borrowArr = new JSONArray();
			Iterator<Request> borrowRequestIterator = borrowQueue.iterator();
			convertQueueToJSONARR(borrowRequestIterator, borrowArr);
			PriorityQueue<Request> returnQueue = currentBookQueue.getValue().getReturnRequestQueue();
			Iterator<Request> returnRequestIterator = returnQueue.iterator();
			JSONArray returnArr = new JSONArray();
			convertQueueToJSONARR(returnRequestIterator, returnArr);
			if (borrowArr.length() > 0 || returnArr.length() > 0) {
				bookQueueNode.put("borrowRequest", borrowArr);
				bookQueueNode.put("returnRequest", returnArr);
				result.put(bookQueueNode);
			}
		}
		return new ResponseEntity<>(result.toString(), HttpStatus.OK);
	}


	private void convertQueueToJSONARR(Iterator<Request> iterator, JSONArray jsonArr) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		while (iterator.hasNext()) {
			Request currentRequest = iterator.next();
			JSONObject requestData = new JSONObject(mapper.writeValueAsString(currentRequest));
			User user = currentRequest.getUser();
			requestData.put("user", new JSONObject(mapper.writeValueAsString(user)));
			requestData.remove("bookDetail");
			jsonArr.put(requestData);
		}

	}


}
