package com.fpt.edu.common.request_queue_simulate;

import com.fpt.edu.common.enums.ERequestStatus;
import com.fpt.edu.common.enums.ERequestType;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.Request;
import com.fpt.edu.exceptions.NotFoundException;
import com.fpt.edu.services.RequestServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RequestQueueManager implements Observer {
    protected final Logger LOGGER = LogManager.getLogger(getClass());
    Map<Long, RequestQueue> requestMap;


    private RequestServices requestServices;

    public RequestQueueManager(RequestServices requestServices) {
        this.requestServices = requestServices;
        requestMap = new HashMap<>();
        List<Request> unProcessRequestList = requestServices.getListPendingRequest();
        for (int i = 0; i < unProcessRequestList.size(); i++) {
            Request currentRequest = unProcessRequestList.get(i);
            if (currentRequest.getBookDetail() != null) {
                long bookDetailId = currentRequest.getBookDetail().getId();
                // check if map not contain a bookDetail id as a key then add new queue with id is book details ID
                if (requestMap.get(bookDetailId) == null) {
                    RequestQueue requestQueue = new RequestQueue();
                    requestQueue.setBookDetailID(bookDetailId);
                    if (currentRequest.getType() == ERequestType.BORROWING.getValue() && currentRequest.getStatus() == ERequestStatus.PENDING.getValue()) {
                        requestQueue.getBorrowRequestQueue().add(currentRequest);
                    } else if (currentRequest.getType() == ERequestType.RETURNING.getValue() && currentRequest.getStatus() == ERequestStatus.PENDING.getValue()) {
                        requestQueue.getReturnRequestQueue().add(currentRequest);
                    }
                    requestMap.put(bookDetailId, requestQueue);// Add request queue to map
                } else {
                    RequestQueue requestQueue = requestMap.get(bookDetailId);
                    if (currentRequest.getType() == ERequestType.BORROWING.getValue() && currentRequest.getStatus() == ERequestStatus.PENDING.getValue()) {
                        requestQueue.getBorrowRequestQueue().add(currentRequest);
                    } else if (currentRequest.getType() == ERequestType.RETURNING.getValue() && currentRequest.getStatus() == ERequestStatus.PENDING.getValue()) {
                        requestQueue.getReturnRequestQueue().add(currentRequest);
                    }
                    requestMap.put(bookDetailId, requestQueue);// Add request queue to map
                }
            }
        }
        LOGGER.info("Initial queue success, number of queue is " + requestMap.size());
    }
    public void addNewRequestToQueue(Request request) {
        long bookDetailId = request.getBookDetail().getId();
        if (requestMap.get(bookDetailId) == null) {
            RequestQueue requestQueue = new RequestQueue();
            requestQueue.setBookDetailID(bookDetailId);
            // if pending then add it to queue
            if (request.getStatus() == ERequestStatus.PENDING.getValue()) {
                if (request.getType() == ERequestType.BORROWING.getValue()) {
                    requestQueue.getBorrowRequestQueue().add(request);
                } else if (request.getType() == ERequestType.RETURNING.getValue()) {
                    requestQueue.getReturnRequestQueue().add(request);
                }
            }
            this.requestMap.put(bookDetailId, requestQueue);
        } else {
            if (request.getStatus() == ERequestStatus.PENDING.getValue()) {
                RequestQueue requestQueue = this.requestMap.get(bookDetailId);
                if (request.getType() == ERequestType.BORROWING.getValue()) {
                    requestQueue.getBorrowRequestQueue().add(request);
                } else if (request.getType() == ERequestType.RETURNING.getValue()) {
                    requestQueue.getReturnRequestQueue().add(request);
                }
            }
        }
    }
    private boolean updateRequest(Request request) throws NotFoundException {
        long bookDetailId = request.getBookDetail().getId();
        if (this.requestMap.get(bookDetailId) == null) {
            throw new NotFoundException("Request not found in Request Queue Hub");
        }
        RequestQueue requestQueue = this.requestMap.get(bookDetailId);
        PriorityQueue<Request> queueToProcess = null;
        if (request.getType() == ERequestType.BORROWING.getValue()) {
            queueToProcess = requestQueue.getBorrowRequestQueue();
        } else if (request.getType() == ERequestType.RETURNING.getValue()) {
            queueToProcess = requestQueue.getReturnRequestQueue();
        }
        Iterator<Request> iterator = queueToProcess.iterator();
        while (iterator.hasNext()) {
            Request currentRequest = iterator.next();
            if (currentRequest.getId() == request.getId()) {
                if (request.getStatus() == ERequestStatus.MATCHING.getValue()) {
                    currentRequest.setStatus(ERequestStatus.MATCHING.getValue());
                } else if (request.getStatus() == ERequestStatus.PENDING.getValue()) {
                    currentRequest.setStatus(ERequestStatus.PENDING.getValue());
                } else if (request.getStatus() == ERequestStatus.PENDING.getValue()) {
                    queueToProcess.remove(currentRequest);
                }
                return true;
            }
        }
        return false;
    }
    public Request findTheMatch(Request request) throws NotFoundException {
        long bookDetailId = request.getBookDetail().getId();
        if (this.requestMap.get(bookDetailId) == null) {
            throw new NotFoundException("Request not found in Request Queue Hub");
        }
        RequestQueue requestQueue = this.requestMap.get(bookDetailId);
        PriorityQueue<Request> queueToProcess = null;
        if (request.getType() == ERequestType.BORROWING.getValue()) {
            queueToProcess = requestQueue.getReturnRequestQueue();
        } else if (request.getType() == ERequestType.RETURNING.getValue()) {
            queueToProcess = requestQueue.getBorrowRequestQueue();
        }
        if (queueToProcess.size() > 0) {
            return queueToProcess.poll();
            // peak is not remove element
           // return queueToProcess.peek();
        }
        return null;
    }
    public Request removeRequestOutTheQueue(Request request) throws NotFoundException {
        long bookDetailId = request.getBookDetail().getId();
        if (this.requestMap.get(bookDetailId) == null) {
            throw new NotFoundException("Request not found in Request Queue Hub");
        }
        RequestQueue requestQueue = this.requestMap.get(bookDetailId);
        PriorityQueue<Request> queueToProcess = null;
        if (request.getType() == ERequestType.BORROWING.getValue()) {
            queueToProcess = requestQueue.getBorrowRequestQueue();
        } else if (request.getType() == ERequestType.RETURNING.getValue()) {
            queueToProcess = requestQueue.getReturnRequestQueue();
        }
        Iterator<Request> iterator = queueToProcess.iterator();
        while (iterator.hasNext()) {
            Request currentRequest = iterator.next();
            if (currentRequest.getId() == request.getId()) {
                    queueToProcess.remove(currentRequest);
        }
    }
        return request;
    }
    @Override
    public void doUpdate(Message mess) throws NotFoundException {
        if (mess.getAction().equalsIgnoreCase(Constant.ACTION_ADD_NEW)) {
                this.addNewRequestToQueue((Request) mess.getMessage());
        } else if (mess.getAction().equalsIgnoreCase(Constant.ACTION_UPDATE)) {
            this.updateRequest((Request) mess.getMessage());
        }
    }

	public Map<Long, RequestQueue> getRequestMap() {
		return requestMap;
	}

	public void setRequestMap(Map<Long, RequestQueue> requestMap) {
		this.requestMap = requestMap;
	}
}
