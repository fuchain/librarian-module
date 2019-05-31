package com.fpt.edu.services;

import com.fpt.edu.common.RequestStatus;
import com.fpt.edu.entities.*;
import com.fpt.edu.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.fpt.edu.entities.Author;
import com.fpt.edu.entities.Book;
import com.fpt.edu.entities.BookDetail;
import com.fpt.edu.repository.BookDetailRepository;
import com.fpt.edu.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	@Autowired
	private BookDetailRepository bookDetailRepository;

    @Autowired
    private RequestRepository requestRepository;


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

    @Transactional
    public List<Book> getRequiringBookList(Long userId) {
        List<Book> bookList = new ArrayList<>();

        List<Request> requestList =
                (List<Request>) requestRepository.findRequestByUserIdAndStatus(userId, RequestStatus.REQUIRING.getValue());
        for (Request request : requestList) {
            Book book = bookRepository.findById(request.getId()).get();
            bookList.add(book);
        }

        return bookList;
    }

    @Transactional
    public List<Book> getReturningBookList(Long userId) {
        List<Book> bookList = new ArrayList<>();

        List<Request> requestList =
                (List<Request>) requestRepository.findRequestByUserIdAndStatus(userId, RequestStatus.RETURNING.getValue());
        for (Request request : requestList) {
            Book book = bookRepository.findById(request.getId()).get();
            bookList.add(book);
        }

        return bookList;
    }
}
