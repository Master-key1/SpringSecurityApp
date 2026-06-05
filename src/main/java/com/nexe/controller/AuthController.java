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

/**
 * AUTH CONTROLLER
 *
 * PURPOSE:
 * - Handles login/authentication requests
 * - Validates user credentials
 * - Generates JWT token on successful login
 *
 * FLOW:
 * Client → /authenticate → Spring Security AuthenticationManager → JWT generation → Response
 */
@RestController
public class AuthController {

    /**
     * AUTHENTICATION MANAGER
     *
     * PURPOSE:
     * - Core Spring Security component
     * - Validates username + password against UserDetailsService + PasswordEncoder
     */
    @Autowired
    AuthenticationManager authenticationManager;

    /**
     * PASSWORD ENCODER
     *
     * NOTE:
     * - Not used directly here (Spring uses it internally via AuthenticationManager)
     * - Can be removed unless used elsewhere (e.g., registration)
     */
    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * JWT UTILITY
     *
     * PURPOSE:
     * - Generates JWT token after successful authentication
     */
    @Autowired
    JwtUtil jwtUtil;

    /**
     * LOGIN / AUTHENTICATION ENDPOINT
     *
     * PURPOSE:
     * - Accepts username/password from client
     * - Authenticates credentials
     * - Returns JWT token if valid
     */
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest authRequest) {

        Authentication auth = null;

        try {

            /**
             * STEP 1: AUTHENTICATE USER CREDENTIALS
             *
             * - Delegates to AuthenticationManager
             * - Internally checks:
             *   -> User exists (UserDetailsService)
             *   -> Password matches (PasswordEncoder)
             */
            auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );

            /**
             * STEP 2: GENERATE JWT TOKEN
             *
             * - Only created after successful authentication
             * - Token contains username as subject
             */
            String jwtToken = jwtUtil.generateToken(authRequest.getUsername());

            /**
             * STEP 3: BUILD RESPONSE
             *
             * - Returns token + basic user info
             * - Client will store token and send in Authorization header
             */
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Authentication successful");
            response.put("token", jwtToken);
            response.put("username", auth.getName());
            response.put("isAuthenticated", auth.isAuthenticated());

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException ex) {

            /**
             * HANDLES INVALID LOGIN CREDENTIALS
             *
             * - Wrong username/password
             * - Authentication fails at Spring Security level
             */
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "message", "Invalid username or password"
                    ));

        } catch (Exception ex) {

            /**
             * GENERIC ERROR HANDLING
             *
             * - Covers unexpected errors (DB failure, config issues, etc.)
             */
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", "Authentication failed"
                    ));
        }
    }
}