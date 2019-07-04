package com.fpt.edu.services;

import com.bigchaindb.constants.Operations;
import com.bigchaindb.model.Transaction;
import com.fpt.edu.entities.Book;
import com.fpt.edu.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Service
public class BookServices {
	final private BookRepository bookRepository;
	final private BigchainTransactionServices bigchainTransactionServices;

	@PersistenceContext
	private EntityManager entityManager;

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

	public Book getByUserAndBookDetail(Long userId, Long bookDetailId) {
		List<Book> bookList = (List<Book>) bookRepository.getByUserAndBookDetail(userId, bookDetailId);
		return bookList.size() > 0 ? bookList.get(0) : null;
	}

	public long countNumberOfBookDetails() {
		return bookRepository.count();
	}

	public long getBookTotalAtLibrary(Long bookDetailId, Long userId) {
		return bookRepository.getBookTotalAtLibrary(bookDetailId, userId);
	}

}
