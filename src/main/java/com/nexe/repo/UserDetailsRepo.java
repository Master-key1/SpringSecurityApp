package com.nexe.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nexe.entity.User;

public interface UserDetailsRepo extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
    
    
}