package com.fpt.edu.repository;

import org.springframework.data.repository.CrudRepository;

import com.fpt.edu.entities.Request;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends CrudRepository<Request, Long> {
}
