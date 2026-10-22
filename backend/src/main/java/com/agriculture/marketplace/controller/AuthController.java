package com.agriculture.marketplace.controller;

import com.agriculture.marketplace.model.User;
import com.agriculture.marketplace.service.AuthService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthService auth;

	public AuthController(AuthService auth) {
		this.auth = auth;
	}

	public record LoginRequest(String email, String password) {
	}

	@PostMapping("/register")
	public Map<String, Object> register(@RequestBody User user) {
		return publicUser(auth.register(user));
	}

	@PostMapping("/login")
	public Map<String, Object> login(@RequestBody LoginRequest req) {
		return publicUser(auth.login(req.email(), req.password()));
	}

	private Map<String, Object> publicUser(User u) {
		return Map.of("userId", u.getUserId(), "fullName", u.getFullName(), "email", u.getEmail(), "role", u.getRole());
	}
}
