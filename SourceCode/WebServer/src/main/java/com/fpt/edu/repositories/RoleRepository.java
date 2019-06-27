package com.fpt.edu.repositories;

import org.springframework.data.repository.CrudRepository;

import com.fpt.edu.entities.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

}
