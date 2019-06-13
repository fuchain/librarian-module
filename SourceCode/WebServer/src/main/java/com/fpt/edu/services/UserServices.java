package com.fpt.edu.services;

import com.fpt.edu.repository.RequestRepository;
import org.apache.commons.collections.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.fpt.edu.entities.Book;
import com.fpt.edu.entities.BookDetail;
import com.fpt.edu.repository.BookRepository;

import com.fpt.edu.entities.User;
import com.fpt.edu.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServices {
	private final BCryptPasswordEncoder encoder;
	private UserRepository userRepository;
	private BookRepository bookRepository;

	public UserServices(BCryptPasswordEncoder encoder, UserRepository userRepository, BookRepository bookRepository) {
		this.encoder = encoder;
		this.userRepository = userRepository;
		this.bookRepository = bookRepository;
	}
	public User addNewUser(User user) {
		user.setDisabled(false);
		if (user.getPassword() != null) {
			user.setPassword(encoder.encode(user.getPassword()));
		} else {
			user.setPassword(null);
		}
		return userRepository.save(user);
	}

	public Optional<User> findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public User getUserByEmail(String email) throws UsernameNotFoundException {
		Optional<User> optionalUser = userRepository.findByEmail(email.toLowerCase());
		User user = null;
		if (optionalUser.isPresent()) {
			user = optionalUser.get();
		}
		if (user == null) {
			throw new UsernameNotFoundException("User not found!");
		}

		return user;
	}

	@Transactional
	public List<Book> getCurrentBookListOfUser(Long userId) {
		List<Book> currentBookList = (List<Book>) bookRepository.findBookListByUserId(userId);
		for (Book book : currentBookList) {
			BookDetail bookDetail = book.getBookDetail();
			bookDetail.getAuthors().size();
		}

		return currentBookList;
	}

	public User updateUser(User user) {
		return userRepository.save(user);
	}

	public List<User> getAllUsers(Pageable pageable) {
		return userRepository.findAll(pageable).getContent();
	}

	public User getByUserId(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new EntityNotFoundException("User id: " + userId + " not found"));
	}


	public int countNumberOfBookThatUserKeep(Long userId){
		return bookRepository.getBookNumberOfCurrentUser(userId);
	}



}
