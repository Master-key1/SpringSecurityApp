package com.nexe.service;

import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nexe.entity.User;
import com.nexe.exception.UserAlreadyExitsException;
import com.nexe.repo.UserDetailsRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDetailsRepo userDetailsRepo;
    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(UserDetailsRepo userDetailsRepo,
                                    PasswordEncoder passwordEncoder) {
        this.userDetailsRepo = userDetailsRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        return userDetailsRepo.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + username));
    }

    public User saveUserDetails(User user) {

        if (userDetailsRepo.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExitsException("Username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userDetailsRepo.save(user);
    }
}