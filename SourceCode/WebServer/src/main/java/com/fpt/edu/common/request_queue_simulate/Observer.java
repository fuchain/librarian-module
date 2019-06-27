package com.fpt.edu.common.request_queue_simulate;

import com.fpt.edu.exceptions.NotFoundException;

public interface Observer  {

    void doUpdate(Message mess) throws NotFoundException;



}
