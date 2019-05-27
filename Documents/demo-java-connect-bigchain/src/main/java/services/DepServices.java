package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entities.Department;
import repository.IDepartment;
@Service
public class DepServices {

	
	@Autowired
	private IDepartment repo;
	
	public boolean saveDep(Department d){
		repo.save(d);
		return true;
		
	}
	
	
}
