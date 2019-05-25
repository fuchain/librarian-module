package com.fpt.edu.repository;

import org.springframework.data.repository.CrudRepository;

import com.fpt.edu.entities.Author;

public interface AuthorRepository extends CrudRepository<Author, Long> {

}
