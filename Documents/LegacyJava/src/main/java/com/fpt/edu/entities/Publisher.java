package com.fpt.edu.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "publisher")
public class Publisher extends AbstractTimestampEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String name;
	@OneToMany(mappedBy = "publisher", cascade = { CascadeType.PERSIST })
	private List<BookDetail> bookDetails;

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

	public List<BookDetail> getBookDetails() {
		return bookDetails;
	}

	public void setBookDetails(List<BookDetail> bookDetails) {
		this.bookDetails = bookDetails;
	}
}
