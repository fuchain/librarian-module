package com.fpt.edu.web.repository;

import org.springframework.data.repository.CrudRepository;

import com.fpt.edu.entities.Author;

public interface AuthorRepository extends CrudRepository<Author, Long> {

}
