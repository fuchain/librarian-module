package com.fpt.edu.web.repository;

import org.springframework.data.repository.CrudRepository;

import com.fpt.edu.entities.Book;

public interface BookRepository extends CrudRepository<Book, Long> {

}
