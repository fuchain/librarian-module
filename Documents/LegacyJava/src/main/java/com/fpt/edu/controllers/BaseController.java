package com.fpt.edu.controllers;

import com.fpt.edu.common.helpers.ImportHelper;
import com.fpt.edu.common.helpers.UtilHelper;
import com.fpt.edu.common.request_queue_simulate.PublishSubscribe;
import com.fpt.edu.common.request_queue_simulate.RequestQueueManager;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.User;
import com.fpt.edu.services.*;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Component
public class BaseController {
	protected final Logger LOGGER;
	protected final UserServices userServices;
	protected final BookDetailsServices bookDetailsServices;
	protected final BookServices bookServices;
	protected final ImportHelper importHelper;
	protected final MatchingServices matchingServices;
	protected final RequestServices requestServices;
	protected final PublishSubscribe publishSubscribe;
	protected final RequestQueueManager requestQueueManager;
	protected final NotificationService notificationService;
	@Autowired
	protected UtilHelper utils;
	protected BigchainTransactionServices bigchainTransactionServices;
	@Autowired
	HttpServletRequest httpServletRequest;

	@Autowired
	public BaseController(UserServices userServices,
	                      BookDetailsServices bookDetailsServices,
	                      BookServices bookServices, ImportHelper importHelper,
	                      MatchingServices matchingServices, RequestServices requestServices,
	                      PublishSubscribe publishSubscribe, RequestQueueManager requestQueueManager,
						  NotificationService notificationService) {
		LOGGER = LogManager.getLogger(getClass());
		this.userServices = userServices;
		this.bookDetailsServices = bookDetailsServices;
		this.bookServices = bookServices;
		this.importHelper = importHelper;
		this.matchingServices = matchingServices;
		this.requestServices = requestServices;
		this.publishSubscribe = publishSubscribe;
		this.requestQueueManager = requestQueueManager;
		this.notificationService = notificationService;
	}

	@ApiOperation(value = "Get all email of user has role librarian", response = JSONObject.class)
	@GetMapping(value = "/publics/getLibrarian", produces = Constant.APPLICATION_JSON)
	public ResponseEntity<List<User>> getLibrarian() {
		return new ResponseEntity<>(userServices.getAllLibrarian(), HttpStatus.OK);
	}
}
