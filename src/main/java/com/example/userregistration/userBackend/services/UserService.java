package com.example.userregistration.userBackend.services;

import java.util.Optional;

import com.example.userregistration.userBackend.dto.UserRequestDTO;
import com.example.userregistration.userBackend.dto.UserResponse;
import com.example.userregistration.userBackend.entity.User;

public interface UserService {

     public Optional<User> findByEmail(String email);
     public UserResponse save(UserRequestDTO user);


    
}
