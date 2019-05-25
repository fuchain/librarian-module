package com.fpt.edu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpt.edu.entities.User;
import com.fpt.edu.repository.UserRepository;

@Service
public class UserServices {

	@Autowired
	private UserRepository userRepository;
	
	public boolean save(User u){
		try{
		userRepository.save(u);
		}catch(Exception e){
			return false;
		}
		return true;
		
	}
	
	
}
