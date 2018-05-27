package com.stud.servlet.models;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = -7198674556101718020L;
	private Long id;
	private String firstName;
	private String lastName;
	private String login;
	private String email;
	private String password;
	
	public User() {}
	
	public User(Long id, String firstName, String lastName, String login, String email, String password) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.login = login;
		this.email = email;
		this.password = password;
	}
	
	public User(String firstName, String lastName, String login, String email, String password) {
		this(null, firstName, lastName, login, email, password);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
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
}
