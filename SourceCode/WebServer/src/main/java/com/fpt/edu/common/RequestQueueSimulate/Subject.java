package com.fpt.edu.common.RequestQueueSimulate;

import com.fpt.edu.exception.NotFoundException;

public interface Subject {

    Message getMessage();
    void notifyToSub() throws NotFoundException;

}
