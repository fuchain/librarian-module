package com.fpt.edu.common;

import com.fpt.edu.entities.Book;
import com.fpt.edu.entities.BookDetail;
import com.fpt.edu.entities.Request;
import com.fpt.edu.entities.User;
import com.fpt.edu.exception.NotFoundException;
import com.fpt.edu.services.RequestServices;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

@Component
public class RequestQueueManagerHub {

    Map<Long, RequestQueueManager> requestMap;



    private RequestServices requestServices;

    public RequestQueueManagerHub(RequestServices requestServices) {
        this.requestServices = requestServices;
        requestMap = new HashMap<>();
        List<Request> unProcessRequestList = this.requestServices.getListPendingRequest();
        for (int i = 0; i < unProcessRequestList.size(); i++) {
            Request currentRequest = unProcessRequestList.get(i);
            long bookDetailId = currentRequest.getBookDetail().getId();
            // check if map not contain a bookDetail id as a key then add new queue with id is book details ID
            if (requestMap.get(bookDetailId) == null) {
                RequestQueueManager requestQueueManager = new RequestQueueManager();
                requestQueueManager.setBookDetailID(bookDetailId);
                if (currentRequest.getType() == ERequestType.BORROWING.getValue() && currentRequest.getStatus() == ERequestStatus.PENDING.getValue()) {
                    requestQueueManager.getBorrowRequestQueue().add(currentRequest);
                } else if (currentRequest.getType() == ERequestType.RETURNING.getValue() && currentRequest.getStatus() == ERequestStatus.PENDING.getValue()) {
                    requestQueueManager.getBorrowRequestQueue().add(currentRequest);
                }
            } else {
                RequestQueueManager requestQueueManager = requestMap.get(bookDetailId);
                requestQueueManager.setBookDetailID(bookDetailId);
                if (currentRequest.getType() == ERequestType.BORROWING.getValue() && currentRequest.getStatus() == ERequestStatus.PENDING.getValue()) {
                    requestQueueManager.getBorrowRequestQueue().add(currentRequest);
                } else if (currentRequest.getType() == ERequestType.RETURNING.getValue() && currentRequest.getStatus() == ERequestStatus.PENDING.getValue()) {
                    requestQueueManager.getBorrowRequestQueue().add(currentRequest);
                }
            }
        }
    }


    public Request getRevertSideRequest(long bookDetailId, int requestType) throws NotFoundException {
        if (this.requestMap.get(bookDetailId) != null) {
            RequestQueueManager requestQueueManager = this.requestMap.get(bookDetailId);
            PriorityQueue<Request> revertSideQueue;
            if (requestType == ERequestType.BORROWING.getValue()) {
                revertSideQueue = requestQueueManager.getReturnRequestQueue();
            } else {
                revertSideQueue = requestQueueManager.getBorrowRequestQueue();
            }
            if (revertSideQueue.size() == 0) {
                return null;
            } else {
                return revertSideQueue.peek();
            }
        } else {
            throw new NotFoundException("Can not Find Book with id" + bookDetailId);
        }
    }

    public void addNewRequestToQueue(User u, Book book, BookDetail bookDetail, int status, int type) {
        Request request = new Request();
        request.setUser(u);
        request.setBook(book);
        request.setBookDetail(bookDetail);
        request.setStatus(status);
        request.setType(type);
        RequestQueueManager queueRequestManager;
        if (this.requestMap.get(bookDetail.getId()) != null) {
            queueRequestManager = new RequestQueueManager();
            queueRequestManager.setBookDetailID(bookDetail.getId());
        } else {
            queueRequestManager = this.requestMap.get(bookDetail.getId());
        }
        if (type == ERequestType.BORROWING.getValue()) {
            queueRequestManager.getBorrowRequestQueue().add(request);
        } else if (type == ERequestType.RETURNING.getValue()) {
            queueRequestManager.getReturnRequestQueue().add(request);
        }
        this.requestServices.saveRequest(request);
    }


}