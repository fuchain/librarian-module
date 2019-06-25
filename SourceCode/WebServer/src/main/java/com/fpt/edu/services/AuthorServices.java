package com.fpt.edu.services;

import com.fpt.edu.entities.Author;
import com.fpt.edu.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorServices {
	private AuthorRepository authorRepository;

	@Autowired
	public AuthorServices(AuthorRepository authorRepository) {
		this.authorRepository = authorRepository;
	}


	public Author getAndAddIfNotExist(String name) {
		Author au = authorRepository.getAuthorByName(name);
		if (au == null) {
			au = new Author();
			au.setName(name);
			authorRepository.save(au);
		}
		return au;
 	}


}
