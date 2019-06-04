package com.fpt.edu.common;

import com.fpt.edu.entities.Request;
import com.fpt.edu.services.RequestServices;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Component
public class RequestQueueManager {

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
                    requestMap.put(bookDetailId,requestQueue);// Add request queue to map
                } else {
                    RequestQueue requestQueue = requestMap.get(bookDetailId);
                    if (currentRequest.getType() == ERequestType.BORROWING.getValue() && currentRequest.getStatus() == ERequestStatus.PENDING.getValue()) {
                        requestQueue.getBorrowRequestQueue().add(currentRequest);
                    } else if (currentRequest.getType() == ERequestType.RETURNING.getValue() && currentRequest.getStatus() == ERequestStatus.PENDING.getValue()) {
                        requestQueue.getReturnRequestQueue().add(currentRequest);
                    }
                    requestMap.put(bookDetailId,requestQueue);// Add request queue to map
                }

            }
        }
        System.out.println("gg");
    }
}
