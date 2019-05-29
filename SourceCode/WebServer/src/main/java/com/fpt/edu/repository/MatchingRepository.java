package com.fpt.edu.repository;

import org.springframework.data.repository.CrudRepository;

import com.fpt.edu.entities.Matching;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingRepository extends CrudRepository<Matching, Long>{

}
