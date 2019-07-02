package com.fpt.edu.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "book_detail")
public class BookDetail extends AbstractTimestampEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "name", columnDefinition = "text")
	private String name;

	@ManyToMany()
	@JoinTable(name = "book_author", joinColumns = { @JoinColumn(name = "book_id") }, inverseJoinColumns = {
			@JoinColumn(name = "author_id") })
	private List<Author> authors;

	@ManyToOne
	@JoinColumn(name = "category_id")
	// @JoinTable(name = "book_category", joinColumns = {@JoinColumn(name =
	// "book_id")}, inverseJoinColumns = {@JoinColumn(name = "category_id")})
	private Category category;
	@ManyToOne
	@JoinColumn(name = "publisher_id")
	private Publisher publisher;

	// new data that return by google
	@Column(name = "libol")
	private String libol;
	@Column(name = "subject_code")
	private String subjectCode;
	@Column(name = "previewLink", columnDefinition = "text")
	private String previewLink;
	@Column(name = "thumbnail", columnDefinition = "text")
	private String thumbnail;
	@Column(name = "published_date", nullable = true)
	private String publishedDate;
	@Column(name = "description", columnDefinition = "text")
	private String description;

	@JsonIgnore
	public Category getCategory() {
		return category;
	}

	@JsonProperty
	public List<String> getParseedSubjectCode() {
		ArrayList<String> parsedCode = new ArrayList<>();
		String[] code = this.subjectCode.split("/");
		for (int i = 0; i < code.length; i++) {
			String subject = code[i];
			subject = subject.replace("\n", "");
			parsedCode.add(subject.trim());
		}

		if (parsedCode.size() == 2 && parsedCode.get(0).equalsIgnoreCase("N")
				&& parsedCode.get(1).equalsIgnoreCase("A")) {
			return new ArrayList<>();
		}

		return parsedCode;
	}

	@OneToMany(mappedBy = "bookDetail", cascade = { CascadeType.PERSIST })
	@JsonIgnore
	private List<Book> books;

	@Column(name = "ISBN")
	private String isbn;

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getSubjectCode() {
		return this.subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public String getDescription() {
		if (description.equalsIgnoreCase("Description Not Available")) {
			return "Chưa có mô tả cho quyển sách này";
		}

		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getLibol() {
		return libol;
	}

	public void setLibol(String libol) {
		this.libol = libol;
	}

	public String getPreviewLink() {
		return previewLink;
	}

	public void setPreviewLink(String previewLink) {
		this.previewLink = previewLink;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(String publishedDate) {
		this.publishedDate = publishedDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public Category getCategories() {
		return category;
	}

	public void setCategories(Category category) {
		this.category = category;
	}

	@JsonIgnore
	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}
}
