package com.nexe.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nexe.securityapp.dto.AuthRequest;
import com.nexe.util.JwtUtil;

@RestController
public class AuthController {
	
	@Autowired
	AuthenticationManager authenticationManager ;
	@Autowired
	 PasswordEncoder passwordEncoder ;
	@Autowired
	JwtUtil jwtUtil ;
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticate(@RequestBody AuthRequest authRequest) {

		  Authentication auth  =null;
	    try {

	         auth =
	                authenticationManager.authenticate(
	                        new UsernamePasswordAuthenticationToken(
	                                authRequest.getUsername(),
	                                authRequest.getPassword()));

	        String jwtToken = jwtUtil.generateToken(authRequest.getUsername());

	        Map<String, Object> response = new HashMap();
	        response.put("message", "Authentication successful");
	        response.put("token", jwtToken);
	        response.put("username", auth.getName());
	        response.put("isAuthenticated",auth.isAuthenticated());

	        return ResponseEntity.ok(response);

	    } catch (BadCredentialsException ex) {

	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body(Map.of(
	                        "message", "Invalid username or password"));

	    } catch (Exception ex) {

	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Map.of(
	                        "message", "Authentication failed"));
	    }
	}
}
