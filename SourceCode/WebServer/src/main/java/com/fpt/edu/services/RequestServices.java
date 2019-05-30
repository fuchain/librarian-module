package com.fpt.edu.services;


import com.fpt.edu.entities.Request;
import com.fpt.edu.repository.RequestRepository;
import org.apache.commons.collections.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestServices {
    @Autowired
    private RequestRepository requestRepository;

    public List<Request> getListRequest() {
        return IteratorUtils.toList(requestRepository.findAll().iterator());

    }
}
