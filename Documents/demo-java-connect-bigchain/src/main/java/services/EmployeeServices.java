package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entities.Employee;
import repository.IEmployee;
@Service
public class EmployeeServices {

	@Autowired
	private IEmployee repo;
	
	
	public void save(Employee e) {
		repo.save(e);
	}
	
	
	
}
