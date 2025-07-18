package com.example.userregistration.userBackend.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.example.userregistration.userBackend.dto.PhoneRequestDTO;
import com.example.userregistration.userBackend.dto.UserRequestDTO;
import com.example.userregistration.userBackend.dto.UserResponse;
import com.example.userregistration.userBackend.entity.Phone;
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
    public UserResponse save(UserRequestDTO userRequestDTO){

        UserResponse userResponse = new UserResponse();
        LocalDateTime now = LocalDateTime.now();

        User user = new User(userRequestDTO.getName(),userRequestDTO.getEmail(), userRequestDTO.getPassword());
        List<Phone> phones = new ArrayList<>();

        userRequestDTO.getPhones().forEach(phoneRequestDTO -> {
            Phone phone = new Phone(phoneRequestDTO.getNumber(), phoneRequestDTO.getCitycode(), phoneRequestDTO.getCountrycode()
            ,user);
            phones.add(phone);
        });

        user.setPhones(phones);
        user.setModified(now);
        user.setLastLogin(now);
        user.setActive(true);
        User userBD =userRepository.save(user);

        userResponse.setId(userBD.getId());
        userResponse.setCreated(userBD.getCreated());
        userResponse.setModified(userBD.getModified());
        userResponse.setLastLogin(userBD.getLastLogin());
        userResponse.setActive(userBD.isActive());
        userResponse.setName(userBD.getName());
        userResponse.setEmail(userBD.getEmail());
        userResponse.setPassword(userBD.getPassword());

        List<PhoneRequestDTO> phonesDB = new ArrayList<>();
        userBD.getPhones().forEach(phone -> {
          PhoneRequestDTO phoneRequestDB = new PhoneRequestDTO(phone.getNumber(),phone.getCitycode(), phone.getCountrycode());
          phonesDB.add(phoneRequestDB);
        });
        userResponse.setPhones(phonesDB);

        return userResponse;

    }



}
