package com.fpt.edu.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fpt.edu.entities.Matching;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingRepository extends CrudRepository<Matching, Long> {
    @Query(value = "SELECT m FROM Matching m WHERE (m.returnerRequest.id = :requestId OR m.borrowerRequest.id = :requestId)" +
            " AND m.status <> :status")
    Matching getByRequestId(@Param("requestId") Long requestId,
                            @Param("status") int matchingStatus);

    @Query(value = "SELECT m FROM Matching m WHERE (m.returnerRequest.id = :requestId OR m.borrowerRequest.id = :requestId)")
    Matching getMatchByRequestId(@Param("requestId") Long requestId);


}
