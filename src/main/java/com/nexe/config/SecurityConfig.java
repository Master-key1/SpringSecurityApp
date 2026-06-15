package com.nexe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
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
     * - Controls public vs secured endpoints
     * - Registers JWT filter before authentication filter
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           JwtAuthFilter jwtAuthFilter) throws Exception {

        http
            // Disable CSRF for stateless REST APIs
            .csrf(csrf -> csrf.disable())

            // IMPORTANT: Stateless session (JWT based auth)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // H2 console support (DEV ONLY)
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))

            // Authorization rules
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/register",
                    "/login",
                    "/authenticate",
                    "/test",
                    "/h2-console/**"
                ).permitAll()

                // ROLE_ADMIN expected in authorities as ROLE_ADMIN
                .requestMatchers("/users").hasAuthority("ROLE_ADMIN")

                .anyRequest().authenticated()
            );

        // Add JWT filter before Spring Security authentication filter
        http.addFilterBefore(jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * PASSWORD ENCODER
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AUTHENTICATION MANAGER (FIXED FOR SPRING SECURITY 6)
     *
     * FIX:
     * - DaoAuthenticationProvider constructor no longer accepts UserDetailsService
     * - Must use setter injection
     */
    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        // Set UserDetailsService (NEW WAY)
        provider.setUserDetailsService(userDetailsService);

        // Set password encoder
        provider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(provider);
    }
}