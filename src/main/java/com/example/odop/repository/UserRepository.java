package com.example.odop.repository;

import com.example.odop.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findById(String id);
    Optional<Users> findByUserId(Long userId);
    Boolean existsById(String id);
}
