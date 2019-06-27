package com.fpt.edu.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.fpt.edu.entities.BookDetail;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookDetailRepository extends PagingAndSortingRepository<BookDetail, Long> {
    List<BookDetail> findByName(String name);

    @Query(value = "SELECT b FROM BookDetail b WHERE lower(b.name) LIKE %:name%")
	Page<BookDetail> findBookDetailsByName(@Param("name") String name, Pageable pageable);

}
