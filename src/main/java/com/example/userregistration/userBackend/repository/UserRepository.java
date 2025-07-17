package com.example.userregistration.userBackend.repository;

import java.util.Optional;
//import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.userregistration.userBackend.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
}
