package com.fpt.edu.services;

import com.fpt.edu.entities.Matching;
import com.fpt.edu.repositories.MatchingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class MatchingServices {
	private final MatchingRepository matchingRepository;

	@Autowired
	public MatchingServices(MatchingRepository matchingRepository) {
		this.matchingRepository = matchingRepository;
	}

	public Matching getMatchingById(Long id) {
		return matchingRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Matching id: " + id + " not found"));
	}

	public Matching updateMatching(Matching matching) {
		return matchingRepository.save(matching);
	}

	public Matching getByReturnRequestId(Long returnRequestId, int matchingStatus1, int matchingStatus2) {
		return matchingRepository.getByReturnRequestId(returnRequestId, matchingStatus1, matchingStatus2);
	}

	public Matching getByReceiveRequestId(Long receiveRequestId, int matchingStatus1, int matchingStatus2) {
		return matchingRepository.getByReceiveRequestId(receiveRequestId, matchingStatus1, matchingStatus2);
	}

	public Matching getByRequestId(Long requestId, int status1, int status2) {
		return matchingRepository.getByRequestId(requestId, status1, status2);
	}

	public Matching getByPin(String pin, int status1, int status2) {
		Matching matching = matchingRepository.getByPin(pin, status1, status2);
		return matching;
	}

	public Matching getByBookId(Long bookId, int status1, int status2) {
		return matchingRepository.getByBookId(bookId, status1, status2);
	}

	public Matching saveMatching(Matching matching) {
		return matchingRepository.save(matching);
	}

//	public void deleteMatching(Long matchingId){
//		matchingRepository.deleteById(matchingId);
//	}

}
