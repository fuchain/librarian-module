package com.fpt.edu.services;

import com.fpt.edu.common.RequestStatus;
import com.fpt.edu.entities.*;
import com.fpt.edu.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServices {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RequestRepository requestRepository;

    public boolean save(User u) {
        try {
            userRepository.save(u);
        } catch (Exception e) {
            return false;
        }
        return true;

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


}
