package com.fpt.edu.repository;

import org.springframework.data.repository.CrudRepository;

import com.fpt.edu.entities.Request;

public interface RequestRepository extends CrudRepository<Request, Long> {

}