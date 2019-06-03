package com.fpt.edu.common;

import com.fpt.edu.entities.Request;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class RequestQueueManager {

    private Long bookDetailID;

    private PriorityQueue<Request> borrowRequestQueue;
    private PriorityQueue<Request> returnRequestQueue;
    Comparator<Request> createDateComparator = new Comparator<Request>() {
        @Override
        public int compare(Request o1, Request o2) {
            return -1;
        }
    };

    public RequestQueueManager() {
        borrowRequestQueue = new PriorityQueue<>(createDateComparator);
        returnRequestQueue = new PriorityQueue<>(createDateComparator);
    }

    public Long getBookDetailID() {
        return bookDetailID;
    }

    public void setBookDetailID(Long bookDetailID) {
        this.bookDetailID = bookDetailID;
    }


    public PriorityQueue<Request> getBorrowRequestQueue() {
        return borrowRequestQueue;
    }

    public void setBorrowRequestQueue(PriorityQueue<Request> borrowRequestQueue) {
        this.borrowRequestQueue = borrowRequestQueue;
    }

    public PriorityQueue<Request> getReturnRequestQueue() {
        return returnRequestQueue;
    }

    public void setReturnRequestQueue(PriorityQueue<Request> returnRequestQueue) {
        this.returnRequestQueue = returnRequestQueue;
    }
}
