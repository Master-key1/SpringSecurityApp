package com.nexe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nexe.entity.User;
import com.nexe.exception.UserAlreadyExitsException;
import com.nexe.repo.UserDetailsRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	UserDetailsRepo userDetailsRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userDetailsRepo.findByUsername(username);
	}

	public User saveUserDetails(User user) {

	    if (userDetailsRepo.existsByUsername(user.getUsername())) {
	        throw new UserAlreadyExitsException("Username already exists");
	    }
	   // user.setPassword(passwordEncoder.encode(user.getPassword()));
	    return userDetailsRepo.save(user);
	}

}
