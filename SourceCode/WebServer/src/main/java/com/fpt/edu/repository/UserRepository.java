package com.fpt.edu.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fpt.edu.entities.User;

import java.util.Optional;


@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    @Query(value = "SELECT u FROM User u WHERE  lower(u.email) = :email")
    Optional<User> findByEmail(@Param("email") String email);




//	@Query(value = "SELECT u FROM User u WHERE  lower(u.email) = :email")
//	Page<User> findByEmail(@Param("email") String email);
}
