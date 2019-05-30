package com.fpt.edu.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fpt.edu.entities.User;
import com.fpt.edu.repository.UserRepository;

@Service
public class UserServices {
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder encoder;

	public UserServices(UserRepository userRepository, BCryptPasswordEncoder encoder) {
		this.userRepository = userRepository;
		this.encoder = encoder;
	}

	public User addNewUser(User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		userRepository.save(user);

		return user;
	}
}
