package com.fpt.edu.common.RequestQueueSimulate;

import com.fpt.edu.exception.NotFoundException;

import java.util.List;

public class PublishSubscribe implements Subject {
    private Message message;
    List<Observer> observerList;
    public void setMessage(Message message) {
        this.message = message;
    }
    public List<Observer> getObserverList() {
        return observerList;
    }
    public void setObserverList(List<Observer> observerList) {
        this.observerList = observerList;
    }
    @Override
    public Message getMessage() {
        return this.message;
    }
    @Override
    public void notifyToSub() throws NotFoundException {
        for (int i = 0; i < this.observerList.size(); i++) {
            this.observerList.get(i).doUpdate(this.message);
        }
    }

    public PublishSubscribe(List<Observer> observerList) {
        this.observerList = observerList;
    }
}
