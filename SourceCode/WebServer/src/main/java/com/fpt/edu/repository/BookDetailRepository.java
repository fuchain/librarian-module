package com.fpt.edu.repository;

import com.fpt.edu.entities.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fpt.edu.entities.BookDetail;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookDetailRepository extends CrudRepository<BookDetail, Long> {

    @Query(value = "SELECT * FROM book_detail WHERE name = ?1", nativeQuery = true)
    BookDetail findByName(String name);

    @Query(value = "SELECT b FROM BookDetail b WHERE lower(b.name) LIKE %:name%")
    List<BookDetail> findBookDetailsByName(@Param("name") String name);
}
