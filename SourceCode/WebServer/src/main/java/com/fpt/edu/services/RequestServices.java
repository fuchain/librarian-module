package com.fpt.edu.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.edu.entities.Request;
import com.fpt.edu.repository.RequestRepository;
import org.apache.commons.collections.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class RequestServices {
    @Autowired
    private RequestRepository requestRepository;

    public List<Request> getListRequest() {
        return IteratorUtils.toList(requestRepository.findAll().iterator());

    }

    public Request getRequestById(Long id) {
        return requestRepository.findById(id).get();
    }

    public Request requestBook(String requestStr) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Request request = objectMapper.readValue(requestStr, Request.class);
        Request requestResult = requestRepository.save(request);
        return requestResult;
    }
}
