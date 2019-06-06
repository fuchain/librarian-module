package com.fpt.edu.services;

import com.fpt.edu.entities.Matching;
import com.fpt.edu.repository.MatchingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

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

    public Matching getByReturnRequestId(Long returnRequestId, int matchingStatus) {
        return matchingRepository.getByReturnRequestId(returnRequestId, matchingStatus);
    }

    public Matching getByReceiveRequestId(Long receiveRequestId, int matchingStatus) {
        return matchingRepository.getByReceiveRequestId(receiveRequestId, matchingStatus);
    }

    public Matching getByRequestId(Long requestId) {
        return matchingRepository.getByRequestId(requestId);
    }

}
