package com.nexe.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.nexe.entity.User;

public interface UserDetailsRepo extends JpaRepository<User,Long> {
	
	public UserDetails findByUsername(String username);

    boolean existsByUsername(String username);

	
	

}
