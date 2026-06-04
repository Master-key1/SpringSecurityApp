package com.nexe.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
	private final String SECRET = "my-very-secure-secret-key-123456";
	private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());
	private final long EXPIRATION_TIME = 1000*60*60; 

	public String generateToken(String username) {
		
		  return Jwts.builder()
	                .subject(username)
	                .issuedAt(new Date())
	                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
	                .signWith(key)
	                .compact();
	}
}
