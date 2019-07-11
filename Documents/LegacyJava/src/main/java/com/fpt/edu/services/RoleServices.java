package com.fpt.edu.services;

import com.fpt.edu.entities.Role;
import com.fpt.edu.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class RoleServices {

	@Autowired
	private RoleRepository roleRepository;

	public Role getById(Long id) {
		return roleRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Role id: " + id + " not found"));
	}

	public Role addRole(Role role) {
		return roleRepository.save(role);
	}

	public Role updateRole(Role role) {
		return roleRepository.save(role);
	}

	public void deleteRole(Long id) {
		roleRepository.deleteById(id);
	}

	public Role getRoleByName(String name) {
		return roleRepository.getRoleByRoleName(name);
	}
}
