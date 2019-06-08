package com.fpt.edu.services;

import com.fpt.edu.entities.Book;
import com.fpt.edu.repository.BookRepository;
import org.apache.commons.collections.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class BookServices {
	private BookRepository bookRepository;

	@Autowired
	public BookServices(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	public Book getBookById(Long id) {
		return bookRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Book id: " + id + " not found"));
	}

	public Book saveBook(Book book) {
		return bookRepository.save(book);
	}

	public Book updateBook(Book book) {
		return bookRepository.save(book);
	}

	public void deleteBook(Long id) {
		bookRepository.deleteById(id);
	}

	public List<Book> getListBookByBookDetailId(Long bookDetailId) {
		return IteratorUtils.toList(bookRepository.findBookListByBookDetailId(bookDetailId).iterator());
	}
}
