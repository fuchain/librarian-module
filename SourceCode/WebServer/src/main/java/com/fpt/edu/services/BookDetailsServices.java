package com.fpt.edu.services;

import com.fpt.edu.common.helper.ReflectionHelper;
import com.fpt.edu.entities.BookDetail;
import com.fpt.edu.exception.EntityNotFoundException;
import com.fpt.edu.repository.BookDetailRepository;
import org.apache.commons.collections.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

@Service
public class BookDetailsServices {
    @Autowired
    private BookDetailRepository bookDetailRepository;

    public List<BookDetail> getAllBookDetails() {
        return IteratorUtils.toList(bookDetailRepository.findAll().iterator());
    }

    public BookDetail saveBookDetail(BookDetail bookDetail) throws IOException {
        bookDetailRepository.save(bookDetail);
        return bookDetail;
    }

    public BookDetail getBookById(Long id) {
        Optional<BookDetail> optionalBookDetail = bookDetailRepository.findById(id);

        BookDetail bookDetail = null;

        if (optionalBookDetail.isPresent()) {
            bookDetail = optionalBookDetail.get();
        }

        return bookDetail;
    }

    public BookDetail updateBookDetail(BookDetail bookDetail) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, EntityNotFoundException {
        BookDetail bookDetailInDB = getBookById(bookDetail.getId());

        if (bookDetail == null) {
            throw new EntityNotFoundException("Book detail id: " + bookDetail.getId() + " not found");
        }

        ReflectionHelper.partialUpdate(bookDetailInDB, bookDetail);
        bookDetailRepository.save(bookDetailInDB);
        return bookDetailInDB;
    }

    public boolean deleteBookDetail(Long id) throws IOException {
        bookDetailRepository.deleteById(id);
        return true;
    }

    public BookDetail getBookDetailByName(String name) {
        BookDetail bookDetail = bookDetailRepository.findByName(name).get(0);
        return bookDetail;
    }

    public List<BookDetail> searchBookDetails(String name) {
        return bookDetailRepository.findBookDetailsByName(name);
    }
}
