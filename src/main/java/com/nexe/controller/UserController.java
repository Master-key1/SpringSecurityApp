package com.nexe.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.nexe.entity.User;
import com.nexe.repo.UserDetailsRepo;
import com.nexe.securityapp.dto.AuthRequest;
import com.nexe.service.CustomUserDetailsService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {

	@Autowired
	CustomUserDetailsService customUserDetailsService;

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody User user) {

		if (user.getUsername() == null && 
				user.getPassword() == null) {
			
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(" Bad Request....");
		}
		User userDet = customUserDetailsService.saveUserDetails(user);

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body("Account created Sucessfully...." + userDet);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthRequest request) {

		String username = request.getUsername();
		String password = request.getPassword();
		UserDetails user = customUserDetailsService.loadUserByUsername(username);

		if (user == null)
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body("Login details not found ....");

		return ResponseEntity
				.status(HttpStatus.OK)
				.body("Login Sucessfully...."+user);

	}

	@GetMapping("/users")
	public ResponseEntity<?> getAllUsers() {

		return ResponseEntity.ok(List.of(Map.of("id", 1, "name", "John"), Map.of("id", 2, "name", "Jane")));
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<?> getUserById(@PathVariable Long id) {

		return ResponseEntity.ok(Map.of("id", id, "name", "John Doe", "email", "john@example.com"));
	}

	@PostMapping("/users")
	public ResponseEntity<?> createUser(@RequestBody Map<String, Object> user) {

		return ResponseEntity.ok(Map.of("message", "User created", "user", user));
	}

	@PutMapping("/users/{id}")
	public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> user) {

		return ResponseEntity.ok(Map.of("message", "User updated", "id", id, "user", user));
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) {

		return ResponseEntity.ok(Map.of("message", "User deleted", "id", id));
	}

	@GetMapping("/search")
	public ResponseEntity<?> searchUsers(@RequestParam String keyword) {

		return ResponseEntity.ok(Map.of("keyword", keyword, "results", List.of("John", "Johnny")));
	}

	@PostMapping("/change-password")
	public ResponseEntity<?> changePassword(@RequestBody Map<String, String> request) {

		return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
	}

	@PostMapping("/upload")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {

		return ResponseEntity.ok(Map.of("fileName", file.getOriginalFilename(), "size", file.getSize()));
	}

	@GetMapping("/admin/users")
	public ResponseEntity<?> getAdminUsers() {

		return ResponseEntity.ok(List.of(Map.of("id", 1, "role", "ADMIN"), Map.of("id", 2, "role", "USER")));
	}

	@PostMapping("/admin/create-user")
	public ResponseEntity<?> createAdminUser(@RequestBody Map<String, Object> request) {

		return ResponseEntity.ok(Map.of("message", "Admin user created", "data", request));
	}

	@PatchMapping("/users/{id}/status")
	public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam String status) {

		return ResponseEntity.ok(Map.of("id", id, "status", status));
	}
}
