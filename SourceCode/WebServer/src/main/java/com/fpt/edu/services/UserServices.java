package com.fpt.edu.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.fpt.edu.entities.Author;
import com.fpt.edu.entities.Book;
import com.fpt.edu.entities.BookDetail;
import com.fpt.edu.repository.AuthorRepository;
import com.fpt.edu.repository.BookDetailRepository;
import com.fpt.edu.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpt.edu.entities.User;
import com.fpt.edu.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserServices {
	private final BCryptPasswordEncoder encoder;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private BookDetailRepository bookDetailRepository;

	@Autowired
	private AuthorRepository authorRepository;



	public UserServices(UserRepository userRepository, BCryptPasswordEncoder encoder) {
		this.userRepository = userRepository;
		this.encoder = encoder;
	}
	public User addNewUser(User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		userRepository.save(user);
		return user;
	}

    public List<Book> getCurrentBookListOfUser(Long userId) {
        List<Book> result = (List<Book>) bookRepository.findBookListByUserId(userId);
        for (int i = 0; i < result.size(); i++) {
            BookDetail bookDetail = result.get(i).getBookDetail();
           bookDetail.getAuthors().size();
            List<Author> authorList=bookDetail.getAuthors();
            System.out.println(authorList.size());
        }
        return result;
    }
}
