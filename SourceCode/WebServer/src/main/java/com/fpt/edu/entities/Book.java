package com.fpt.edu.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="book")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Book {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@ManyToOne()
	@JoinColumn(name="book_id")
	private BookDetail bookDetail;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JsonIgnore
	private List<Transaction> transactions;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JsonIgnore
	private List<Request> requests;

	@ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="user_id")
	@JsonIgnore
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}
	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
	public List<Request> getRequests() {
		return requests;
	}
	public void setRequests(List<Request> requests) {
		this.requests = requests;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public BookDetail getBookDetail() {
		return bookDetail;
	}
	public void setBookDetail(BookDetail bookDetail) {
		this.bookDetail = bookDetail;
	}

	
	
	
	
}
