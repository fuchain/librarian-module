package com.fpt.edu.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fpt.edu.entities.Role;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

	@Query("select  role from Role role where  role.name =:name")
	Role getRoleByRoleName(@Param("name") String name);

}
