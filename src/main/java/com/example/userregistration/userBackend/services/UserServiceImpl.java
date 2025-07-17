package com.example.userregistration.userBackend.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.example.userregistration.userBackend.dto.UserResponse;
import com.example.userregistration.userBackend.entity.User;
import com.example.userregistration.userBackend.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

    private  UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Transactional(readOnly=true)
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    @Transactional
    public UserResponse save(User user){

        UserResponse userResponse = new UserResponse();
        LocalDateTime now = LocalDateTime.now();
        user.getPhones().forEach(phone ->
          phone.setUser(user)
        );
        User userBD =userRepository.save(user);

        userResponse.setId(userBD.getId());
        userResponse.setCreated(now);

        return userResponse;

    }



}
