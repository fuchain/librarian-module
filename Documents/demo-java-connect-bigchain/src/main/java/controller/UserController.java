package controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import entities.Department;
import entities.Employee;
import entities.User;
import services.DepServices;
import services.EmployeeServices;
import services.UseServices;

@RestController
@RequestMapping("user")
public class UserController {

	
	@Autowired
	private UseServices services;
	@Autowired
	private EmployeeServices empService;
	@Autowired
	private DepServices depServices;
	
	
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test() {
		services.saveUser(new User("PhongDoan", "123456"));
		Department dep = new Department();
		depServices.saveDep(dep);
//		dep.setDeparmentName("Insurance");
//		List<Employee> list = new ArrayList<>();
//		Employee em1= new Employee();
//		em1.setEmployeeName("phong doan");
//		Employee em2= new Employee();
//		em1.setEmployeeName("Linh Nguyen");
//		Employee em3= new Employee();
//		em1.setEmployeeName("Xuyen Nguyen");
//		
//		list.add(em1);
//		list.add(em2);
//		list.add(em3);
//		dep.setEmployess(list);
//		depServices.saveDep(dep);
		
		
		
		
		
		return "a";
	}
	
	
	
}
