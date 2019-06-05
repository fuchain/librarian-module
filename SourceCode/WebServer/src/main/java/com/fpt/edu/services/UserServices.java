package com.fpt.edu.services;

import com.fpt.edu.common.ERequestType;
import com.fpt.edu.entities.Request;
import com.fpt.edu.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.fpt.edu.entities.Book;
import com.fpt.edu.entities.BookDetail;
import com.fpt.edu.repository.BookRepository;

import com.fpt.edu.entities.User;
import com.fpt.edu.repository.UserRepository;

import java.util.ArrayList;
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
    private RequestRepository requestRepository;

    public UserServices(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public void addNewUser(User user) {
        if (!user.getPassword().trim().isEmpty()) {
            user.setPassword(encoder.encode(user.getPassword()));
        } else {
            user.setPassword(null);
        }

        userRepository.save(user);
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

        for (int i = 0; i < currentBookList.size(); i++) {
            BookDetail bookDetail = currentBookList.get(i).getBookDetail();
            bookDetail.getAuthors().size();
        }

        return currentBookList;
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }
}
