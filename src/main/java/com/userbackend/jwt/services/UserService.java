package com.userbackend.jwt.services;

import java.util.Optional;

import com.userbackend.jwt.dto.UserRequestDTO;
import com.userbackend.jwt.dto.UserResponseDTO;
import com.userbackend.jwt.entity.User;

public interface UserService {

     public Optional<User> findByEmail(String email);
     public UserResponseDTO save(UserRequestDTO user);
     public Optional<UserResponseDTO> loadUser(String authorizationHeader);


    
}
