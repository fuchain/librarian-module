package com.fptedu.web.repository;

import org.springframework.data.repository.CrudRepository;

import com.fpt.edu.entities.User;

public interface UserRepository extends CrudRepository<User, Long>{

}
