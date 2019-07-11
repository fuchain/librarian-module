package com.fpt.edu.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fpt.edu.constant.Constant;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "tbl_user")
public class User extends AbstractTimestampEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "email", unique = true)
	private String email;

	@JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "password")
	private String password;

	@Column(name = "fullname")
	private String fullName;

	@Column(name = "phone")
	private String phone;

	@ManyToOne
	@JoinColumn(name = "role_id")
	@JsonIgnore
	private Role role;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.PERSIST, mappedBy = "user")
	private List<Book> listBooks;

	@Column(name = "isDisabled")
	private Boolean isDisabled;

	public User() {
	}

	public User(String email, String password, String fullName, String phone) {
		this.email = email;
		this.password = password;
		this.fullName = fullName;
		this.phone = phone;
	}

	public List<Book> getListBooks() {
		return listBooks;
	}

	public void setListBooks(List<Book> listBooks) {
		this.listBooks = listBooks;
	}

	public Boolean isDisabled() {
		return isDisabled;
	}

	public void setDisabled(Boolean disabled) {
		isDisabled = disabled;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		for (String prefix : Constant.FPT_EMAIL_PREFIXS) {
			fullName = fullName.replace(prefix, Constant.EMPTY_VALUE);
		}
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
