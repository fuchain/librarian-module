package com.fpt.edu.service;

import com.bigchaindb.constants.Operations;
import com.bigchaindb.model.Transaction;
import com.fpt.edu.entity.Book;
import com.fpt.edu.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;

@Service
public class BookServices {
	final private BookRepository bookRepository;
	final private BigchainTransactionServices bigchainTransactionServices;

	@Autowired
	public BookServices(BookRepository bookRepository, BigchainTransactionServices bigchainTransactionServices) {
		this.bookRepository = bookRepository;
		this.bigchainTransactionServices = bigchainTransactionServices;
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

	public List<Book> getListBookByBookDetailIdWithFilter(
		Long bookDetailId,
		String transferStatus, Pageable pageable
	) {
		return bookRepository.findBookListByBookDetailIdWithFilter(
			bookDetailId,
			transferStatus,
			pageable
		).getContent();
	}

	public List<Book> getListBookByBookDetailId(Long bookDetailId, Pageable pageable) {
		return bookRepository.findBookListByBookDetailId(bookDetailId, pageable).getContent();
	}

	public List<Transaction> getListTransactionsFromBigchain(Book book) throws Exception {
		book.setBcTransactionList(
			bigchainTransactionServices.getTransactionsByAssetId(
				book.getAssetId(), Operations.TRANSFER
			).getTransactions()
		);
		return book.getBcTransactionList();
	}

	public Transaction getLastTransactionFromBigchain(Book book) throws Exception {
		Transaction transaction = bigchainTransactionServices.getTransactionById(book.getLastTxId());
		book.setBcLastTransaction(transaction);
		book.getAsset().setData((Map<String, String>) transaction.getAsset().getData());
		book.getMetadata().setData((Map<String, String>) transaction.getMetaData());
		return book.getBcLastTransaction();
	}
}
