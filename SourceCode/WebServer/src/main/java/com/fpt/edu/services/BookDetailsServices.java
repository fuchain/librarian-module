package com.fpt.edu.services;

import com.fpt.edu.common.helper.ReflectionHelper;
import com.fpt.edu.entities.BookDetail;
import com.fpt.edu.repository.BookDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Service
public class BookDetailsServices {
    private final BookDetailRepository bookDetailRepository;

    @Autowired
    public BookDetailsServices(BookDetailRepository bookDetailRepository) {
        this.bookDetailRepository = bookDetailRepository;
    }

    public BookDetail saveBookDetail(BookDetail bookDetail) {
        return bookDetailRepository.save(bookDetail);
    }

    public BookDetail getBookById(Long id) {
        return bookDetailRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book detail id: " + id + " not found"));
    }

    public BookDetail updateBookDetail(BookDetail bookDetail) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, EntityNotFoundException {
        BookDetail bookDetailInDB = getBookById(bookDetail.getId());

        ReflectionHelper.partialUpdate(bookDetailInDB, bookDetail);

        return bookDetailRepository.save(bookDetailInDB);
    }

    public void deleteBookDetail(Long id) {
        bookDetailRepository.deleteById(id);
    }

    public BookDetail getBookDetailByName(String name) {
        return bookDetailRepository.findByName(name).get(0);
    }

    public List<BookDetail> searchBookDetails(String name) {
        return bookDetailRepository.findBookDetailsByName(name);
    }
}
