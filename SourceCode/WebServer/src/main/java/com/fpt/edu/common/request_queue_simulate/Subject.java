package com.fpt.edu.common.request_queue_simulate;

import com.fpt.edu.exceptions.NotFoundException;

public interface Subject {

    Message getMessage();
    void notifyToSub() throws NotFoundException;

}
