package com.fpt.edu.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fpt.edu.entities.Request;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface RequestRepository extends CrudRepository<Request, Long> {
    @Query(value = "SELECT * FROM request where user_id = ?1 AND type = ?2",
            nativeQuery = true)
    Collection<Request> findByUserIdAndType(Long userId, int type);

    @Query(value = "SELECT COUNT(id) FROM request WHERE type = ?1 AND user_id = ?2 " +
            " AND (book_detail_id = ?3 OR book_id = ?4)"
            , nativeQuery = true)
    Integer checkExistedRequest(int type, Long user_id, Long book_detail_id, Long book_id);
}
