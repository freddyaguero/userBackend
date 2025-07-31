package com.userbackend.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


import com.userbackend.jwt.dto.UserRequestDTO;
import com.userbackend.jwt.dto.UserResponseDTO;
import com.userbackend.jwt.services.UserService;
import java.util.Optional;


@RestController
public class UserController {

	@Autowired
    private UserService userService;


	@PostMapping("/sign-up")
    public ResponseEntity<?> createUser( @RequestBody UserRequestDTO user) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
	}


	@GetMapping("/login")
	public ResponseEntity<?> loginUser(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        Optional<UserResponseDTO> userResponseOptional= userService.loadUser( authorizationHeader);
        return ResponseEntity.status(HttpStatus.FOUND).body(userResponseOptional.get());
    }

	
}
