package com.fpt.edu.controllers;

import com.fpt.edu.common.helpers.ImportHelper;
import com.fpt.edu.common.helpers.SchedulerJob;
import com.fpt.edu.common.request_queue_simulate.PublishSubscribe;
import com.fpt.edu.common.request_queue_simulate.RequestQueueManager;
import com.fpt.edu.entities.User;
import com.fpt.edu.exceptions.InvalidExpressionException;
import com.fpt.edu.services.*;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("schedulers")
public class SchedulerController {

	@Autowired
	private UserServices userServices;
	@Autowired
	private RequestServices requestServices;
	@Autowired
	private MatchingServices matchingServices;

	private Logger logger = LoggerFactory.getLogger(BigchainTransactionServices.class);

	@ApiOperation(value = "Enable or disable system scheduler", response = String.class)
	@PutMapping("/on")
	public ResponseEntity<String> enableScheduler(@RequestBody String body, Principal principal) throws InvalidExpressionException, SchedulerException {
		// Check sender is librarian or not
		User librarian = userServices.getUserByEmail(principal.getName());

		// Get data from request body
		JSONObject bodyObject = new JSONObject(body);
		boolean enable = bodyObject.getBoolean("enable");
		String cronExpression = bodyObject.getString("interval_expression");

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

		JobDetail jobDetail = JobBuilder.newJob(SchedulerJob.class).setJobData(jobDataMap).build();

		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("CronTrigger")
			.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();

		scheduler.scheduleJob(jobDetail, trigger);
		scheduler.start();
		logger.info("Started scheduler");

		return new ResponseEntity<>("Started Scheduler Successfully", HttpStatus.OK);
	}

}
