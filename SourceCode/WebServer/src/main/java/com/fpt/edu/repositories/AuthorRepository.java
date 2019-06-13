package com.fpt.edu.repositories;

import org.springframework.data.repository.CrudRepository;

import com.fpt.edu.entities.Author;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {

}
