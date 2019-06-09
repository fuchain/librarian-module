package com.fpt.edu.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

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

	@ManyToMany(cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
	@JsonIgnore
	private List<Role> roles;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
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

	public Boolean getDisabled() {
		return isDisabled;
	}

	public void setDisabled(Boolean disabled) {
		isDisabled = disabled;
	}

	public void setListBooks(List<Book> listBooks) {
		this.listBooks = listBooks;
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

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

}
