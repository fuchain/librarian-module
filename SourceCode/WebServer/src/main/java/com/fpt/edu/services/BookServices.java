package com.fpt.edu.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.edu.entities.Book;
import com.fpt.edu.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class BookServices {
    @Autowired
    private BookRepository bookRepository;

    public Book getBookById(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        Book book = null;

        if (optionalBook.isPresent()) {
            book = optionalBook.get();
        }

        return book;
    }

    public Book saveBook(String bookStr) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        Book book = objectMapper.readValue(bookStr, Book.class);
        Book bookResult = bookRepository.save(book);

        return bookResult;
    }

    public Book updateBook(String bookStr) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        Book book = objectMapper.readValue(bookStr, Book.class);
        Book bookResult = bookRepository.save(book);

        return bookResult;
    }

    public boolean deleteBook(Long id) {
        bookRepository.deleteById(id);
        return true;
    }
}
