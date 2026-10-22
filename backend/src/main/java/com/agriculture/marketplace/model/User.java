package com.agriculture.marketplace.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;
	@Column(nullable = false, length = 100)
	
	private String fullName;
	@Column(nullable = false, unique = true, length = 150)
	private String email;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(nullable = false, length = 255)
	private String password;
	private String phone;
	private String address;
	@Column(nullable = false, length = 20)
	private String role;

	public User() {
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer v) {
		userId = v;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String v) {
		fullName = v;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String v) {
		email = v;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String v) {
		password = v;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String v) {
		phone = v;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String v) {
		address = v;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String v) {
		role = v;
	}
}
