package com.fpt.edu.services;

import com.fpt.edu.entities.Role;
import com.fpt.edu.entities.User;
import com.fpt.edu.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private UserRepository userRepository;

	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> optionalUser = userRepository.findByEmail(username.toLowerCase());
		User user = null;
		if (optionalUser.isPresent()) {
			if (optionalUser.get().getPassword().isEmpty()) {
				throw new UsernameNotFoundException("Password is wrong!");
			}
			user = optionalUser.get();
		}
		if (user == null) {
			throw new UsernameNotFoundException("User not found!");
		}

		Role role = user.getRole();
		Collection<GrantedAuthority> authorities = new HashSet<>();
		if (role != null) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
	}
}
