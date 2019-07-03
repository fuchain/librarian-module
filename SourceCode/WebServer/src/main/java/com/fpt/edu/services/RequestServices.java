package com.fpt.edu.services;


import com.fpt.edu.common.request_queue_simulate.Message;
import com.fpt.edu.common.request_queue_simulate.Observer;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.Request;
import com.fpt.edu.repositories.RequestRepository;
import org.apache.commons.collections.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

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

	public boolean checkExistedRequest(int type, Long userId, int status1, int status2, Long bookDetailId, Long bookId) {
		int row = requestRepository.checkExistedRequest(type, userId, status1, status2, bookDetailId, bookId);
		return row > 0;
	}

	@Transactional
	public List<Request> findByUserIdAndType(Long userId, int type, int status1, int status2) {
		return (List<Request>) requestRepository.findByUserIdAndType(userId, type, status1, status2);
	}

	public Request updateRequest(Request request) {
		return requestRepository.save(request);

	}

	public List<Request> getListPendingRequest() {
		return IteratorUtils.toList(requestRepository.getListOfPendingRequest().iterator());
	}

	public List<Request> getPendingMatchingList(int status1, int status2) {
		return requestRepository.getPendingMatchingList(status1, status2);
	}

	@Override
	public void doUpdate(Message mess) {
		if (mess.getAction().equalsIgnoreCase(Constant.ACTION_ADD_NEW)) {
			saveRequest((Request) mess.getMessage());
		} else if (mess.getAction().equalsIgnoreCase(Constant.ACTION_UPDATE)) {
			updateRequest((Request) mess.getMessage());
		}
	}

	@Transactional
	public int countPendingRequestOfUser(Long userId, int type, int status1, int status2) {
		return requestRepository.getNumerOfRequest(userId, type, status1, status2);
	}

	@Transactional
	public long countRequestByStatus(int status) {
		return requestRepository.countRequestByStatus(status);
	}




}
