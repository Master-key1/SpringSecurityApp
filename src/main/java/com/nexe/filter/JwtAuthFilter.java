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
 * PURPOSE:
 * - Intercepts every incoming HTTP request
 * - Extracts JWT token from Authorization header
 * - Validates token
 * - Sets authenticated user in Spring Security Context
 *
 * WHY IT EXISTS:
 * - Enables stateless authentication (no session required)
 * - Ensures every request is verified using JWT
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * CONSTRUCTOR INJECTION
     *
     * PURPOSE:
     * - Injects required dependencies
     * - JwtUtil → for token operations (parse/validate)
     * - CustomUserDetailsService → loads user from DB
     */
    public JwtAuthFilter(JwtUtil jwtUtil,
                         CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * MAIN FILTER METHOD
     *
     * PURPOSE:
     * - Executes once per request
     * - Extracts and validates JWT token
     * - Authenticates user if token is valid
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {

            /**
             * STEP 1: READ AUTHORIZATION HEADER
             *
             * Expected format:
             * Authorization: Bearer <JWT_TOKEN>
             */
            String authHeader = request.getHeader("Authorization");

            String token = null;
            String username = null;

            /**
             * STEP 2: EXTRACT TOKEN FROM HEADER
             *
             * - Check if header exists
             * - Ensure it starts with "Bearer "
             * - Extract actual JWT token part
             */
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);

                try {
                    /**
                     * STEP 3: EXTRACT USERNAME FROM TOKEN
                     *
                     * - Parses JWT payload
                     * - Gets "subject" (username)
                     */
                    username = jwtUtil.extractUsername(token);

                } catch (ExpiredJwtException e) {
                    /**
                     * TOKEN EXPIRED HANDLING
                     *
                     * - If token is expired → reject request immediately
                     */
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Token expired. Please login again.");
                    return;

                } catch (JwtException e) {
                    /**
                     * INVALID TOKEN HANDLING
                     *
                     * - Covers malformed, tampered, or unsupported tokens
                     */
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Invalid token.");
                    return;
                }
            }

            /**
             * STEP 4: AUTHENTICATE USER (IF NOT ALREADY AUTHENTICATED)
             *
             * Conditions:
             * - Username must exist in token
             * - SecurityContext must not already contain authentication
             */
            if (username != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                /**
                 * LOAD USER DETAILS FROM DATABASE
                 *
                 * - Fetches user roles, password, and authorities
                 */
                UserDetails userDetails =
                        customUserDetailsService.loadUserByUsername(username);

                /**
                 * STEP 5: VALIDATE TOKEN AGAINST USER
                 *
                 * - Ensures token belongs to correct user
                 * - Ensures token is not expired or tampered
                 */
                if (jwtUtil.validateToken(token, userDetails)) {

                    /**
                     * STEP 6: CREATE AUTHENTICATION OBJECT
                     *
                     * - Represents authenticated user in Spring Security
                     * - Contains user identity + roles/authorities
                     */
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    /**
                     * STEP 7: ADD REQUEST DETAILS
                     *
                     * - Attaches IP address, session info, etc.
                     */
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    /**
                     * STEP 8: SET AUTHENTICATION IN SECURITY CONTEXT
                     *
                     * - This marks user as "logged in"
                     * - Required for @PreAuthorize, role checks, etc.
                     */
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

        } catch (Exception ex) {
            /**
             * GLOBAL ERROR HANDLING
             *
             * - Prevents filter chain from breaking
             * - Logs unexpected errors (should ideally use logger instead of printStackTrace)
             */
            ex.printStackTrace();
        }

        /**
         * STEP 9: CONTINUE FILTER CHAIN
         *
         * - Pass request to next filter
         * - Even if no authentication, request continues (may be rejected later)
         */
        filterChain.doFilter(request, response);
    }
}