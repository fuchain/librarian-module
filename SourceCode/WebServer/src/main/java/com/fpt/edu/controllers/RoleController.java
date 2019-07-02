package com.fpt.edu.controllers;

import com.fpt.edu.common.helpers.ImportHelper;
import com.fpt.edu.common.request_queue_simulate.PublishSubscribe;
import com.fpt.edu.common.request_queue_simulate.RequestQueueManager;
import com.fpt.edu.services.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
public class RoleController extends BaseController {

	public RoleController(UserServices userServices, BookDetailsServices bookDetailsServices, BookServices bookServices, ImportHelper importHelper, MatchingServices matchingServices, RequestServices requestServices, TransactionServices transactionServices, PublishSubscribe publishSubscribe, RequestQueueManager requestQueueManager, NotificationService notificationService) {
		super(userServices, bookDetailsServices, bookServices, importHelper, matchingServices, requestServices, transactionServices, publishSubscribe, requestQueueManager, notificationService);
	}

	@ApiOperation(value = "Get role by id", response = String.class)
	@GetMapping("{id}")
	public ResponseEntity<String> getById(@PathVariable Long id) {

		return new ResponseEntity<>("", HttpStatus.OK);
	}
}
