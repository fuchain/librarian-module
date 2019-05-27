package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entities.User;
import repository.IUserRepository;

@Service
public class UseServices {

	
	@Autowired
	private IUserRepository userRepository;
	
	
	public boolean saveUser(User u){
		try{
		userRepository.save(u);
			
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	
}
