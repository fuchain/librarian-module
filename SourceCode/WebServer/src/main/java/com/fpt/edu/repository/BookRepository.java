package com.fpt.edu.repository;

import com.fpt.edu.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

	@Query(value = "SELECT b FROM Book b WHERE b.user.id = :userId")
	Collection<Book> findBookListByUserId(@Param("userId") Long userId);

	@Query(value = "SELECT count (b) FROM Book b WHERE b.user.id = :userId")
	int getBookNumberOfCurrentUser(@Param("userId") Long userId);

	@Query(
		value = "" +
			"SELECT b " +
			"FROM Book b " +
			"WHERE b.bookDetail.id = :bookDetailId AND b.transferStatus = :transferStatus"
	)
	Page<Book> findBookListByBookDetailIdWithFilter(
		@Param("bookDetailId") Long bookDetailId,
		@Param("transferStatus") String transferStatus,
		Pageable pageable);

	@Query(
		value = "" +
			"SELECT b " +
			"FROM Book b " +
			"WHERE b.bookDetail.id = :bookDetailId"
	)
	Page<Book> findBookListByBookDetailId(
		@Param("bookDetailId") Long bookDetailId,
		Pageable pageable);
}
