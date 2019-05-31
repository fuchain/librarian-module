package com.fpt.edu.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fpt.edu.entities.Request;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface RequestRepository extends CrudRepository<Request, Long> {
    @Query(value = "SELECT * FROM request where user_id = ?1 AND status = ?2",
            nativeQuery = true)
    Collection<Request> findRequestByUserIdAndStatus(Long userId, int status);
}
