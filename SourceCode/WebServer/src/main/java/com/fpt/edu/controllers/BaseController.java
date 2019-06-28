package com.fpt.edu.controllers;

import com.fpt.edu.common.helpers.ImportHelper;
import com.fpt.edu.common.helpers.UtilHelper;
import com.fpt.edu.common.request_queue_simulate.PublishSubscribe;
import com.fpt.edu.common.request_queue_simulate.RequestQueueManager;
import com.fpt.edu.services.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class BaseController {
    protected final Logger LOGGER = LogManager.getLogger(getClass());

    @Autowired
    protected UtilHelper utils;

    @Autowired
    HttpServletRequest httpServletRequest;

	protected final UserServices userServices;
	protected final BookDetailsServices bookDetailsServices;
	protected final BookServices bookServices;
	protected final ImportHelper importHelper;
	protected final MatchingServices matchingServices;
	protected final RequestServices requestServices;
	protected final TransactionServices transactionServices;
	protected final PublishSubscribe publishSubscribe;
	protected final RequestQueueManager requestQueueManager;

	@Autowired
	public BaseController(UserServices userServices,
						  BookDetailsServices bookDetailsServices,
						  BookServices bookServices, ImportHelper importHelper,
						  MatchingServices matchingServices, RequestServices requestServices, TransactionServices transactionServices, PublishSubscribe publishSubscribe, RequestQueueManager requestQueueManager) {
		this.userServices = userServices;
		this.bookDetailsServices = bookDetailsServices;
		this.bookServices = bookServices;
		this.importHelper = importHelper;
		this.matchingServices = matchingServices;
		this.requestServices = requestServices;
		this.transactionServices = transactionServices;
		this.publishSubscribe = publishSubscribe;
		this.requestQueueManager = requestQueueManager;
	}


}
