package com.fpt.edu.services;

import com.fpt.edu.entities.Matching;
import com.fpt.edu.repository.MatchingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MatchingServices {

    @Autowired
    private MatchingRepository matchingRepository;

    public Matching getMatchingById(Long id) {
        Optional<Matching> optionalMatching = matchingRepository.findById(id);

        Matching matching = null;

        if (optionalMatching.isPresent()) {
            matching = optionalMatching.get();
        }

        return matching;
    }

    public Matching updateMatching(Matching matching) {
        return matchingRepository.save(matching);
    }

    public Matching getMatchingByReturnRequestId(Long returnRequestId, int matchingStatus) {
        return matchingRepository.getByReturnRequestId(returnRequestId, matchingStatus);
    }

    public Matching getMatchingByReceiveRequestId(Long receiveRequestId, int matchingStatus) {
        return matchingRepository.getByReceiveRequestId(receiveRequestId, matchingStatus);
    }


    public Matching getMatchingByRequestId(Long requestId) {
        return matchingRepository.getMatchByRequestId(requestId);
    }


}
