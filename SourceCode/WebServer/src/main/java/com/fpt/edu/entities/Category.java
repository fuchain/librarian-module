package com.fpt.edu.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "category")
public class Category extends AbstractTimestampEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String name;

	@OneToMany(mappedBy = "category")
	private List<BookDetail> booksDetails;

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
	@JsonIgnore
	public List<BookDetail> getBooksDetails() {
		return booksDetails;
	}

	public void setBooksDetails(List<BookDetail> booksDetails) {
		this.booksDetails = booksDetails;
	}

}
