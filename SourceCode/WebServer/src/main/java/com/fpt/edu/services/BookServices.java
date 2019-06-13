package com.fpt.edu.services;

import com.bigchaindb.constants.Operations;
import com.bigchaindb.model.Transaction;
import com.fpt.edu.entities.Book;
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
		book.setBcLastTransaction(bigchainTransactionServices.getTransactionById(book.getLastTxId()));
		return book.getBcLastTransaction();
	}

	public Map<String, String> getAssetFromBigchain(Book book, Transaction transaction) {
		book.setAsset((Map<String, String>) transaction.getAsset().getData());
		return book.getAsset();
	}

	public Map<String, String> getMetadataFromBigchain(Book book, Transaction transaction) {
		book.setMetadata((Map<String, String>) transaction.getMetaData());
		return book.getMetadata();
	}
}
