package com.fpt.edu.services;


import com.fpt.edu.entities.Request;
import com.fpt.edu.repository.RequestRepository;
import org.apache.commons.collections.IteratorUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RequestServices {
    @Autowired
    private RequestRepository requestRepository;

    public List<Request> getListRequest() {
        return IteratorUtils.toList(requestRepository.findAll().iterator());
    }

    public Request getRequestById(Long id) {
        Optional<Request> optionalRequest = requestRepository.findById(id);

        Request request = null;

        if (optionalRequest.isPresent()) {
            request = optionalRequest.get();
        }

        return request;
    }

    public Request saveRequest(Request request) {
        Request requestResult = requestRepository.save(request);
        return requestResult;
    }

    public boolean checkExistedRequest(int type, Long userId, Long bookDetailId, Long bookId) {
        int row = requestRepository.checkExistedRequest(type, userId, bookDetailId, bookId);
        return row > 0;
    }
    @Transactional
    public List<Request> findByUserIdAndType(Long userId, int type) {

        return (List<Request>) requestRepository.findByUserIdAndType(userId, type);
    }

    public Request updateRequest(Request request) {
        return requestRepository.save(request);
    }


    public List<Request> getListPendingRequest() {
        return IteratorUtils.toList(requestRepository.getListOfPendingRequest().iterator());

    }






}
