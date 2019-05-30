package com.fpt.edu.services;

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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookDetailRepository bookDetailRepository;

    @Autowired
    private AuthorRepository authorRepository;

    public boolean save(User u) {
        try {
            userRepository.save(u);
        } catch (Exception e) {
            return false;
        }
        return true;

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
