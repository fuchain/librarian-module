package com.fpt.edu.services;

import com.fpt.edu.common.RequestType;
import com.fpt.edu.entities.*;
import com.fpt.edu.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.fpt.edu.entities.Book;
import com.fpt.edu.entities.BookDetail;
import com.fpt.edu.repository.BookDetailRepository;
import com.fpt.edu.repository.BookRepository;

import com.fpt.edu.entities.User;
import com.fpt.edu.repository.UserRepository;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServices {
	private final BCryptPasswordEncoder encoder;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookRepository bookRepository;

	public UserServices(UserRepository userRepository, BCryptPasswordEncoder encoder) {
		this.userRepository = userRepository;
		this.encoder = encoder;
	}

	public User addNewUser(User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		userRepository.save(user);
		return user;
	}

	public Optional<User> findUserByUsername(String name) {
	    return userRepository.findByUsername(name);
    }

    @Transactional
    public List<Book> getCurrentBookListOfUser(Long userId) {
        List<Book> result = (List<Book>) bookRepository.findBookListByUserId(userId);
        for (int i = 0; i < result.size(); i++) {
            BookDetail bookDetail = result.get(i).getBookDetail();
            bookDetail.getAuthors().size();
        }
        return result;
    }

    public User getUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        User user = null;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return user;
    }
}
