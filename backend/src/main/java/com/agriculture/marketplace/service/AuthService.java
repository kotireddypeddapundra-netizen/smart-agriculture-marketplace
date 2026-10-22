package com.agriculture.marketplace.service;

import com.agriculture.marketplace.model.User;
import com.agriculture.marketplace.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AuthService {
	private final UserRepository users;
	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	public AuthService(UserRepository users) {
		this.users = users;
	}

	public User register(User user) {
		if (user.getFullName() == null || user.getFullName().isBlank())
			throw new IllegalArgumentException("Full name is required");
		if (user.getEmail() == null || user.getEmail().isBlank())
			throw new IllegalArgumentException("Email is required");
		if (user.getPassword() == null || user.getPassword().isBlank())
			throw new IllegalArgumentException("Password is required");
		if (users.existsByEmail(user.getEmail()))
			throw new IllegalArgumentException("Email already registered");
		String role = user.getRole() == null ? "CUSTOMER" : user.getRole().toUpperCase(Locale.ROOT);
		if (!role.equals("FARMER") && !role.equals("CUSTOMER"))
			throw new IllegalArgumentException("Role must be FARMER or CUSTOMER");
		user.setRole(role);
		user.setPassword(encoder.encode(user.getPassword()));
		return users.save(user);
	}

	public User login(String email, String password) {
		User u = users.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
		if (!encoder.matches(password, u.getPassword()))
			throw new IllegalArgumentException("Invalid email or password");
		return u;
	}
}
