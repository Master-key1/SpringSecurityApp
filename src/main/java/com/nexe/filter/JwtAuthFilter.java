package com.nexe.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nexe.service.CustomUserDetailsService;
import com.nexe.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JWT AUTHENTICATION FILTER
 *
 * PURPOSE: - Intercepts every incoming HTTP request - Extracts JWT token from
 * Authorization header - Validates token - Sets authenticated user in Spring
 * Security Context
 *
 * WHY IT EXISTS: - Enables stateless authentication (no session required) -
 * Ensures every request is verified using JWT
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final CustomUserDetailsService customUserDetailsService;

	/**
	 * CONSTRUCTOR INJECTION
	 *
	 * PURPOSE: - Injects required dependencies - JwtUtil → for token operations
	 * (parse/validate) - CustomUserDetailsService → loads user from DB
	 */
	public JwtAuthFilter(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
		this.jwtUtil = jwtUtil;
		this.customUserDetailsService = customUserDetailsService;
	}

	/**
	 * MAIN FILTER METHOD
	 *
	 * PURPOSE: - Executes once per request - Extracts and validates JWT token -
	 * Authenticates user if token is valid
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {

			String authHeader = request.getHeader("Authorization");

			String token = null;
			String username = null;

			// STEP 1: Extract token
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				token = authHeader.substring(7);

				try {
					username = jwtUtil.extractUsername(token);
					System.out.println("JWT Username: " + username);

				} catch (ExpiredJwtException e) {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().write("Token expired");
					return;

				} catch (JwtException e) {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().write("Invalid token");
					return;
				}
			}

			// STEP 2: Authenticate user
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

				System.out.println("AUTHORITIES: " + userDetails.getAuthorities());

				if (jwtUtil.validateToken(token, userDetails)) {

					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());

					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		filterChain.doFilter(request, response);
	}
}