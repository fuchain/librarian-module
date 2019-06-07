package com.fpt.edu.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fpt.edu.entities.Book;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    @Query(value = "SELECT b FROM Book b WHERE b.user.id = :userId")
    Collection<Book> findBookListByUserId(@Param("userId") Long userId);


	@Query(value = "SELECT count (b) FROM Book b WHERE b.user.id = :userId")
	int getBookNumberOfCurrentUser(@Param("userId") Long userId);

}
