package com.fpt.edu.entities;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "author")
public class Author {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;

	@ManyToMany(mappedBy = "authors",cascade= CascadeType.ALL)
	private List<BookDetail> books;

	public List<BookDetail> getBooks() {
		return books;
	}

	public void setBooks(List<BookDetail> books) {
		this.books = books;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}