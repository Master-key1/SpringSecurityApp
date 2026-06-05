package com.nexe.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        if (user.getUsername() == null || user.getUsername().isBlank()
                || user.getPassword() == null || user.getPassword().isBlank()) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Username and Password are required");
        }

        User savedUser = customUserDetailsService.saveUserDetails(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Account created successfully for user : "
                        + savedUser.getUsername());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {

        try {

            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getUsername(),
                                    request.getPassword()));

            return ResponseEntity.ok(
                    "Login Successful : " + authentication.getName());

        } catch (BadCredentialsException ex) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");

        } catch (Exception ex) {

            ex.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Authentication failed");
        }
    }

   
   

	@GetMapping("/users")
	public ResponseEntity<?> getUserById() {

		return ResponseEntity.ok(Map.of("id", 1, "name", "John Doe", "email", "john@example.com"));
	}

}
