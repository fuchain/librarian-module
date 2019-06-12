package com.fpt.edu.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "transaction")
public class Transaction extends AbstractTimestampEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;

	@ManyToOne
	@JoinColumn(name = "borrower_id")
	private User borrower;

	@ManyToOne
	@JoinColumn(name = "returner_id")
	private User returner;

	private int type;

	@Column(name = "bc_transaction_id")
	private String bcTxId;

	@OneToMany(cascade = {CascadeType.ALL}, mappedBy = "transaction")
	private List<Image> images;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public User getBorrower() {
		return borrower;
	}

	public void setBorrower(User borrower) {
		this.borrower = borrower;
	}

	public User getReturner() {
		return returner;
	}

	public void setReturner(User returner) {
		this.returner = returner;
	}

	public String getBcTxId() {
		return bcTxId;
	}

	public void setBcTxId(String bcTxId) {
		this.bcTxId = bcTxId;
	}
}
