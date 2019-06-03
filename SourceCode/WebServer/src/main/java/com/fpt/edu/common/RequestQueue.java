package com.fpt.edu.common;

import com.fpt.edu.entities.Request;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class RequestQueue {

    private Long bookDetailID;

    private Queue<Request> borrowRequestQueue;
    private Queue<Request> returnRequestQueue;
    Comparator<Request> createDateComparator = new Comparator<Request>() {
        @Override
        public int compare(Request o1, Request o2) {
            if (o1.getCreateDate().getTime() - o2.getCreateDate().getTime() > 0) {
                return -1;
            } else return 1;
        }
    };

    public RequestQueue() {
        borrowRequestQueue = new PriorityQueue<>(createDateComparator);
        returnRequestQueue = new PriorityQueue<>(createDateComparator);
    }

    public Long getBookDetailID() {
        return bookDetailID;
    }

    public void setBookDetailID(Long bookDetailID) {
        this.bookDetailID = bookDetailID;
    }


    public Queue<Request> getBorrowRequestQueue() {
        return borrowRequestQueue;
    }

    public void setBorrowRequestQueue(Queue<Request> borrowRequestQueue) {
        this.borrowRequestQueue = borrowRequestQueue;
    }

    public Queue<Request> getReturnRequestQueue() {
        return returnRequestQueue;
    }

    public void setReturnRequestQueue(Queue<Request> returnRequestQueue) {
        this.returnRequestQueue = returnRequestQueue;
    }
}
