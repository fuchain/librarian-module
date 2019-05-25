package com.fpt.edu.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.fpt.edu.entities.User;


@Repository
public interface UserRepository extends CrudRepository<User, Long>{

}
