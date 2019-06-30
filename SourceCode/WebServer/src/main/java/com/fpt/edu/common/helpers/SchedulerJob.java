package com.fpt.edu.common.helpers;


import com.fpt.edu.common.enums.EMatchingStatus;
import com.fpt.edu.common.enums.ERequestStatus;
import com.fpt.edu.common.enums.ERequestType;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.Matching;
import com.fpt.edu.entities.Request;
import com.fpt.edu.services.MatchingServices;
import com.fpt.edu.services.RequestServices;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SchedulerJob implements Job {

	private RequestServices requestServices;
	private MatchingServices matchingServices;

	private Logger logger = LoggerFactory.getLogger(SchedulerJob.class);

	@Override
	public void execute(JobExecutionContext context) {
		if (requestServices == null || matchingServices == null) {
			JobDataMap jobDataMap = context.getMergedJobDataMap();
			requestServices = (RequestServices) jobDataMap.get("RequestServices");
			matchingServices = (MatchingServices) jobDataMap.get("MatchingServices");
		}

		// Get request list with status is pending or matching
		List<Request> pendingMatchingList = requestServices.getPendingMatchingList(ERequestStatus.PENDING.getValue(),
			ERequestStatus.MATCHING.getValue());

		Date now = new Date();

		// Update status of request that is expired
		for (Request request : pendingMatchingList) {
			if (request.getStatus() == ERequestStatus.PENDING.getValue()) {
				updatePendingRequest(request, now);
			} else if (request.getStatus() == ERequestStatus.MATCHING.getValue()) {
				updateMatchingRequest(request, now);
			}
		}
		logger.info("============== SCHEDULER SCANNED==================");
	}

	private void updatePendingRequest(Request request, Date now) {
		long duration = UtilHelper.getDuration(request.getCreateDate(), now, TimeUnit.DAYS);

		if (duration >= Constant.REQUEST_EXPIRED_TIME) {
			request.setStatus(ERequestStatus.EXPIRED.getValue());
			requestServices.updateRequest(request);

			logger.info("Update request id: " + request.getId() + " at " + now);
		}
	}

	private void updateMatchingRequest(Request request, Date now) {
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
					logger.info("Update request id: " + pairedRequest.getId() + " at " + now);
				}

				logger.info("Update request id: " + request.getId() + " at " + now);
				logger.info("Update matching id: " + matching.getId() + " at " + now);
			}
		}
	}

}
