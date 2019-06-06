package com.fpt.edu.services;


import com.fpt.edu.common.RequestQueueSimulate.Message;
import com.fpt.edu.common.RequestQueueSimulate.Observer;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.Request;
import com.fpt.edu.repository.RequestRepository;
import org.apache.commons.collections.IteratorUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class RequestServices implements Observer {
    private final RequestRepository requestRepository;

    @Autowired
    public RequestServices(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public Request getRequestById(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Request id: " + id + " not found"));
    }

    public Request saveRequest(Request request) {
        return requestRepository.save(request);
    }

    public boolean checkExistedRequest(int type, Long userId, int status, Long bookDetailId, Long bookId) {
        int row = requestRepository.checkExistedRequest(type, userId, status, bookDetailId, bookId);
        return row > 0;
    }

    @Transactional
    public List<Request> findByUserIdAndType(Long userId, int type, int status) {
        return (List<Request>) requestRepository.findByUserIdAndType(userId, type, status);
    }

    public Request updateRequest(Request request) {
        return requestRepository.save(request);
    }

    public List<Request> getListPendingRequest() {
        return IteratorUtils.toList(requestRepository.getListOfPendingRequest().iterator());
    }

    @Override
    public void doUpdate(Message mess) {
        if (mess.getAction().equalsIgnoreCase(Constant.ACTION_ADD_NEW)) {
            saveRequest((Request) mess.getMessage());
        } else if (mess.getAction().equalsIgnoreCase(Constant.ACTION_UPDATE)) {
            updateRequest((Request) mess.getMessage());
        }
    }
}
