package com.fpt.edu.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fpt.edu.entities.Matching;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingRepository extends CrudRepository<Matching, Long> {
	@Query(value = "SELECT m FROM Matching m WHERE m.returnerRequest.id = :returnRequestId" +
		" AND m.status <> :status AND m.status <> 4 AND m.status <> 5")
	Matching getByReturnRequestId(@Param("returnRequestId") Long returnRequestId,
								  @Param("status") int matchingStatus);

	@Query(value = "SELECT m FROM Matching m WHERE m.borrowerRequest.id = :receiveRequestId" +
		" AND m.status <> :status AND m.status <> 4 AND m.status <> 5")
	Matching getByReceiveRequestId(@Param("receiveRequestId") Long receiveRequestId,
								   @Param("status") int matchingStatus);

	@Query(value = "SELECT m FROM Matching m WHERE (m.returnerRequest.id = :requestId OR m.borrowerRequest.id = :requestId)")
	Matching getByRequestId(@Param("requestId") Long requestId);

	@Query(value = "SELECT m FROM Matching m WHERE m.pin = :pin AND m.status <> :status  AND m.status <> 4  AND m.status <> 5")
	Matching getByPin(@Param("pin") String pin,
					  @Param("status") int status);

	@Query(value = "SELECT m FROM Matching m WHERE m.book.id = :bookId AND m.status <> :status  AND m.status <> 4  AND m.status <> 5")
	Matching getByBookId(@Param("bookId") Long bookId,
						 @Param("status") int status);
}
