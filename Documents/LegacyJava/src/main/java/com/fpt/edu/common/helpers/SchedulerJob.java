package com.fpt.edu.common.helpers;


import com.fpt.edu.common.enums.EMatchingStatus;
import com.fpt.edu.common.enums.ERequestStatus;
import com.fpt.edu.common.enums.ERequestType;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.Matching;
import com.fpt.edu.entities.Request;
import com.fpt.edu.services.MatchingServices;
import com.fpt.edu.services.NotificationService;
import com.fpt.edu.services.RequestServices;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SchedulerJob implements Job {

	private final Logger LOGGER = LogManager.getLogger(getClass());
	private RequestServices requestServices;
	private MatchingServices matchingServices;
	private NotificationService notificationService;

	@Override
	public void execute(JobExecutionContext context) {
		if (requestServices == null || matchingServices == null) {
			JobDataMap jobDataMap = context.getMergedJobDataMap();
			requestServices = (RequestServices) jobDataMap.get("RequestServices");
			matchingServices = (MatchingServices) jobDataMap.get("MatchingServices");
			notificationService = (NotificationService) jobDataMap.get("NotificationHelper");
		}

		// Get request list with status is pending or matching
		List<Request> pendingMatchingList = requestServices.getPendingMatchingList(ERequestStatus.PENDING.getValue(),
			ERequestStatus.MATCHING.getValue());

		Date now = new Date();

		// Update status of request that is expired
		try {
			for (Request request : pendingMatchingList) {
				if (request.getStatus() == ERequestStatus.PENDING.getValue()) {
					updatePendingRequest(request, now);
				} else if (request.getStatus() == ERequestStatus.MATCHING.getValue()) {
					updateMatchingRequest(request, now);
				}
			}
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		LOGGER.info("============== SCHEDULER SCANNED==================");
	}

	private void updatePendingRequest(Request request, Date now) throws UnirestException {
		long duration = UtilHelper.getDuration(request.getCreateDate(), now, TimeUnit.DAYS);

		if (duration >= Constant.REQUEST_EXPIRED_TIME) {
			request.setStatus(ERequestStatus.EXPIRED.getValue());
			requestServices.updateRequest(request);

			LOGGER.info("Update request id: " + request.getId() + " at " + now);
			pushNotiFromRequest(request);
			LOGGER.info("Update request id: " + request.getId() + " at " + now);
		}
	}

	private void updateMatchingRequest(Request request, Date now) throws UnirestException {
		// Update matching
		Matching matching = null;
		Request pairedRequest = null;

		if (request.getType() == ERequestType.RETURNING.getValue()) {
			matching = matchingServices.getByReturnRequestId(
				request.getId(),
				EMatchingStatus.PENDING.getValue(),
				EMatchingStatus.PAIRED.getValue()
			);
			if (matching != null) {
				pairedRequest = matching.getBorrowerRequest();
			}
		} else if (request.getType() == ERequestType.BORROWING.getValue()) {
			matching = matchingServices.getByReceiveRequestId(
				request.getId(),
				EMatchingStatus.PENDING.getValue(),
				EMatchingStatus.PAIRED.getValue()
			);
			if (matching != null) {
				pairedRequest = matching.getReturnerRequest();
			}
		}
		if (matching != null) {
			long duration = UtilHelper.getDuration(matching.getCreateDate(), now, TimeUnit.DAYS);

			if (duration >= Constant.MATCHING_EXPIRED_TIME) {

				matching.setStatus(EMatchingStatus.EXPIRED.getValue());
				matchingServices.updateMatching(matching);

				// Update request
				request.setStatus(ERequestStatus.EXPIRED.getValue());
				requestServices.updateRequest(request);

				// Update paired request
				if (pairedRequest != null) {
					pairedRequest.setStatus(ERequestStatus.EXPIRED.getValue());
					requestServices.updateRequest(pairedRequest);
					pushNotiFromRequest(pairedRequest);
					LOGGER.info("Update request id: " + pairedRequest.getId() + " at " + now);
				}

				pushNotiFromRequest(request);
				LOGGER.info("Update request id: " + request.getId() + " at " + now);
				LOGGER.info("Update matching id: " + matching.getId() + " at " + now);
			}
		}
	}

	private void pushNotiFromRequest(Request request) {
		if (!request.getUser().getRole().getName().equals(Constant.ROLES_LIBRARIAN)) {
			if (request.getType() == ERequestType.RETURNING.getValue()) {
				notificationService.pushNotification(
					request.getUser().getEmail(),
					"Hệ thống đã hủy yêu cầu trả sách " + request.getBook().getBookDetail().getName() + " - #" + request.getBook().getId(),
					Constant.NOTIFICATION_TYPE_KEEPING
				);
			} else if (request.getType() == ERequestType.BORROWING.getValue()) {
				notificationService.pushNotification(
					request.getUser().getEmail(),
					"Hệ thống đã hủy yêu cầu mượn sách " + request.getBookDetail().getName(),
					Constant.NOTIFICATION_TYPE_KEEPING
				);
			}
		}
	}

}
