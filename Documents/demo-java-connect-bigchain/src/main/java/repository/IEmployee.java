package repository;

import org.springframework.data.repository.CrudRepository;

import entities.Employee;

public interface IEmployee extends CrudRepository<Employee, Long> {

}
