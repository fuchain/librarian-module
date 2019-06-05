package com.fpt.edu.common.RequestQueueSimulate;

import com.fpt.edu.exception.NotFoundException;

public interface Observer  {

    void doUpdate(Message mess) throws NotFoundException;



}
