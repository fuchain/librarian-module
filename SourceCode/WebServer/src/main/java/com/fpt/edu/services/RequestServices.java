package com.fpt.edu.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.edu.entities.Request;
import com.fpt.edu.entities.User;
import com.fpt.edu.repository.RequestRepository;
import com.fpt.edu.repository.UserRepository;
import org.apache.commons.collections.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
        return requestRepository.findById(id).get();
    }

    public Request saveRequest(Request request) {
        Request requestResult = requestRepository.save(request);
        return requestResult;
    }

    public boolean checkExistedRequest(int status, Long userId, Long bookDetailId, Long bookId) {
        int row = requestRepository.checkExistedRequest(status, userId, bookDetailId, bookId);
        return row > 0;
    }

    public List<Request> findByUserIdAndStatus(Long userId, int status) {
        return (List<Request>) requestRepository.findByUserIdAndStatus(userId, status);
    }

}
