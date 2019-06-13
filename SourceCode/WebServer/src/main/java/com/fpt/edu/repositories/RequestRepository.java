package com.fpt.edu.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fpt.edu.entities.Request;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RequestRepository extends CrudRepository<Request, Long> {
	@Query(value = "SELECT r FROM Request r WHERE r.user.id = :userId AND r.type = :type AND r.status <> :status " +
		"AND r.status <> 4")
	Collection<Request> findByUserIdAndType(@Param("userId") Long userId,
											@Param("type") int type,
											@Param("status") int status);

	@Query(value = "SELECT COUNT(r.id) FROM Request r WHERE r.type = :type AND r.user.id = :user_id " +
		" AND r.status <> :status  AND r.status <> 4" +
		" AND (r.bookDetail.id = :book_detail_id OR r.book.id = :book_id)")
	Integer checkExistedRequest(@Param("type") int type,
								@Param("user_id") Long user_id,
								@Param("status") int status,
								@Param("book_detail_id") Long book_detail_id,
								@Param("book_id") Long book_id);

	@Query(value = "SELECT req from Request  req WHERE req.status=1 and req.mode=1 order by req.createDate asc")
	List<Request> getListOfPendingRequest();


	@Query(value = "SELECT count(r) FROM Request r WHERE r.user.id = :userId AND r.type = :type " +
		"AND r.status <> :status AND r.status <> 4")
	int getNumerOfRequest(@Param("userId") Long userId,
						  @Param("type") int type,
						  @Param("status") int status);


}
