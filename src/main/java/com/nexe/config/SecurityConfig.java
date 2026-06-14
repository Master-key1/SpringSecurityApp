package com.nexe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nexe.filter.JwtAuthFilter;

@Configuration
public class SecurityConfig {

    /**
     * SECURITY FILTER CHAIN
     *
     * PURPOSE:
     * - Defines Spring Security configuration for HTTP requests
     * - Controls which endpoints are public and which require authentication
     * - Registers JWT filter in the request processing chain
     *
     * FUNCTIONALITY:
     * - Disables CSRF (needed for stateless REST APIs)
     * - Allows public access to specific endpoints (register/login/authenticate)
     * - Protects all other endpoints
     * - Ensures JWT filter runs before UsernamePasswordAuthenticationFilter
     *   so that token is validated before Spring Security processes request
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           JwtAuthFilter jwtAuthFilter) throws Exception {

        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Public endpoints - no authentication required
                .requestMatchers(
                        "/register",
                        "/login",
                        "/authenticate",
                        "/h2-console",
                        "/test"
                ).permitAll()

                .requestMatchers("/users").hasAuthority("ROLE_ADMIN")                // Secure all other endpoints
                .anyRequest().authenticated()
            );

        // Add JWT authentication filter into filter chain
        http.addFilterBefore(jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * PASSWORD ENCODER
     *
     * PURPOSE:
     * - Encodes user passwords before saving into database
     * - Ensures passwords are not stored in plain text
     *
     * FUNCTIONALITY:
     * - Uses BCrypt hashing algorithm
     * - Provides one-way encryption (cannot be decrypted)
     * - Used during registration and authentication
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AUTHENTICATION MANAGER
     *
     * PURPOSE:
     * - Core component responsible for authenticating users
     * - Used during login process (username/password validation)
     *
     * FUNCTIONALITY:
     * - Uses DaoAuthenticationProvider to fetch user details from DB
     * - Compares raw password with encoded password
     * - Delegates user lookup to UserDetailsService
     * - Delegates password validation to PasswordEncoder
     */
    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        // Authentication provider that uses database for authentication
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        // Set password encoder for validating passwords
        provider.setPasswordEncoder(passwordEncoder);

        // Wrap provider inside AuthenticationManager
        return new ProviderManager(provider);
    }
}