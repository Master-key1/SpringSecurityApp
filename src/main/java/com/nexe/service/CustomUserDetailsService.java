package com.nexe.service;

import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nexe.entity.User;
import com.nexe.exception.UserAlreadyExitsException;
import com.nexe.repo.UserDetailsRepo;

/**
 * CUSTOM USER DETAILS SERVICE
 *
 * PURPOSE:
 * - Acts as a bridge between Spring Security and the database
 * - Loads user information from the database during authentication
 * - Handles user registration logic
 *
 * WHY IMPLEMENT UserDetailsService?
 * - Spring Security uses this interface to fetch user details
 *   during login.
 * - AuthenticationManager automatically calls
 *   loadUserByUsername() when authenticating a user.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * USER REPOSITORY
     *
     * PURPOSE:
     * - Performs database operations on User table
     * - Used to find and save users
     */
    private final UserDetailsRepo userDetailsRepo;

    /**
     * PASSWORD ENCODER
     *
     * PURPOSE:
     * - Encrypts passwords before storing them
     * - Uses BCryptPasswordEncoder configured in SecurityConfig
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * CONSTRUCTOR INJECTION
     *
     * PURPOSE:
     * - Inject required dependencies
     * - Preferred over field injection (@Autowired)
     */
    public CustomUserDetailsService(UserDetailsRepo userDetailsRepo,
                                    PasswordEncoder passwordEncoder) {
        this.userDetailsRepo = userDetailsRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * LOAD USER BY USERNAME
     *
     * PURPOSE:
     * - Called automatically by Spring Security during login
     * - Fetches user from database using username
     *
     * FLOW:
     * AuthenticationManager
     *         ↓
     * loadUserByUsername()
     *         ↓
     * User fetched from DB
     *         ↓
     * Password comparison happens
     *
     * RETURNS:
     * - User object (implements UserDetails)
     *
     * THROWS:
     * - UsernameNotFoundException if user does not exist
     */
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        return userDetailsRepo.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found: " + username));
    }

    /**
     * REGISTER / SAVE USER
     *
     * PURPOSE:
     * - Creates a new user account
     * - Validates username uniqueness
     * - Encrypts password before storing
     *
     * STEPS:
     * 1. Check if username already exists
     * 2. Encode password using BCrypt
     * 3. Save user in database
     * 4. Return saved user
     */
    public User saveUserDetails(User user) {

        /**
         * CHECK FOR DUPLICATE USERNAME
         *
         * Prevents multiple users from registering
         * with the same username.
         */
        if (userDetailsRepo.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExitsException(
                    "Username already exists");
        }

        /**
         * ENCODE PASSWORD
         *
         * SECURITY:
         * - Converts plain text password into BCrypt hash
         * - Prevents storing raw passwords in database
         */
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        /**
         * SAVE USER TO DATABASE
         */
        return userDetailsRepo.save(user);
    }
}