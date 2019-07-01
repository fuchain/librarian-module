package com.fpt.edu.controllers;

import com.fpt.edu.common.enums.*;
import com.fpt.edu.common.helpers.ImportHelper;
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
import org.aspectj.util.FileUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("librarian")
public class LibrarianController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(LibrarianController.class);
	private String cronExpression = "";

	@Autowired
	private NotificationService notificationService;

	public LibrarianController(UserServices userServices, BookDetailsServices bookDetailsServices, BookServices bookServices, ImportHelper importHelper, MatchingServices matchingServices, RequestServices requestServices, TransactionServices transactionServices, PublishSubscribe publishSubscribe, RequestQueueManager requestQueueManager, NotificationService notificationService) {
		super(userServices, bookDetailsServices, bookServices, importHelper, matchingServices, requestServices, transactionServices, publishSubscribe, requestQueueManager, notificationService);
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
	public ResponseEntity<String> transferBook(
		@RequestParam Long book_detail_id,
		Principal principal
	) throws EntityNotFoundException {
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

		// Get data from request body
		JSONObject bodyObject = new JSONObject(body);
		boolean enable = bodyObject.getBoolean("enable");
		cronExpression = bodyObject.getString("interval_expression");

		// Get scheduler
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

		if (!enable) {
			scheduler.shutdown();
			logger.info("Shut down scheduler");
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
		logger.info("Started scheduler");

		return new ResponseEntity<>("Started Scheduler Successfully", HttpStatus.OK);
	}

	@ApiOperation(value = "Get scheduler status", response = JSONObject.class)
	@GetMapping("/scheduler/status")
	public ResponseEntity<String> getSchedulerStatus(Principal principal) throws SchedulerException {
		// Check sender is librarian or not
		User librarian = userServices.getUserByEmail(principal.getName());

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

	@ApiOperation(value = "Sync current keeper of a book instance back from bigchain", response = Book.class)
	@PutMapping("/books/{book_id}/sync_current_keeper")
	public ResponseEntity<Book> syncCurrentKeeperFromBigchain(
		@PathVariable("book_id") Long bookId
	) throws Exception {
		Book book = bookServices.getBookById(bookId);
		bookServices.getLastTransactionFromBigchain(book);
		User currentKeeper = userServices.getUserByEmail(book.getMetadata().getCurrentKeeper());
		book.setUser(currentKeeper);
		bookServices.updateBook(book);
		return new ResponseEntity<>(book, HttpStatus.OK);
	}

	@ApiOperation(value = "Send notification", response = String.class)
	@PostMapping("/notification")
	public ResponseEntity<String> pushNotification(@RequestBody String body, Principal principal) throws IOException, UnirestException {
		User librarian = userServices.getUserByEmail(principal.getName());

		JSONObject bodyObject = new JSONObject(body);
		String email = bodyObject.getString("email");
		String message = bodyObject.getString("message");
		String type = bodyObject.getString("type");

		notificationService.pushNotification(email, message, type);

		return new ResponseEntity<>("Push notification successfully", HttpStatus.OK);
	}

}
