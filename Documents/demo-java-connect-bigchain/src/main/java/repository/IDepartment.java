package repository;

import org.springframework.data.repository.CrudRepository;

import entities.Department;

public interface IDepartment extends CrudRepository<Department, Long> {

}
