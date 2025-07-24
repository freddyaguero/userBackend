package com.userbackend.jwt.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.transaction.annotation.Transactional;


import org.springframework.stereotype.Service;

import com.userbackend.jwt.dto.PhoneRequestDTO;
import com.userbackend.jwt.dto.UserRequestDTO;
import com.userbackend.jwt.dto.UserResponseDTO;
import com.userbackend.jwt.entity.Phone;
import com.userbackend.jwt.entity.User;
import com.userbackend.jwt.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

    private  UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Transactional(readOnly=true)
    @Override
    public Optional<User> findByEmail(String email) {

        Optional<User> userOptional= userRepository.findByEmail(email);
        return userOptional;
    }


    @Transactional
    public UserResponseDTO save(UserRequestDTO userRequestDTO, String token){

        UserResponseDTO userResponse = new UserResponseDTO();
        LocalDateTime now = LocalDateTime.now();

        User user = new User(userRequestDTO.getName(),userRequestDTO.getEmail(), userRequestDTO.getPassword());
        List<Phone> phones = new ArrayList<>();

        if (userRequestDTO.getPhones().size()!=0){
                userRequestDTO.getPhones().forEach(phoneRequestDTO -> {
                Phone phone = new Phone(phoneRequestDTO.getNumber(), phoneRequestDTO.getCitycode(), phoneRequestDTO.getCountrycode()
                ,user);
                phones.add(phone);
            });
        }


        user.setPhones(phones);
        user.setCreated(now);
        user.setModified(now);
        
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
         if (userBD.getPhones().size()!=0){
            userBD.getPhones().forEach(phone -> {
                PhoneRequestDTO phoneRequestDB = new PhoneRequestDTO(phone.getNumber(),phone.getCitycode(), phone.getCountrycode());
                phonesDB.add(phoneRequestDB);
            });
         }

        userResponse.setPhones(phonesDB);
        userResponse.setToken(token);

        return userResponse;

    }

    @Transactional
    public Optional<UserResponseDTO> loadUser(UserRequestDTO userRequestDTO, String token){

        Optional<UserResponseDTO> userResponseOptional =null;
        UserResponseDTO userResponse=null;

        Optional<User> userOptional=userRepository.findByEmail(userRequestDTO.getEmail());
        if (userOptional.isPresent()) {

            userResponse = new UserResponseDTO();
            User userDB = userOptional.get();

            LocalDateTime now = LocalDateTime.now();
            userDB.setModified(now);
            userDB.setLastLogin(now);
            userRepository.save(userDB);

            userResponse.setId(userDB.getId());
            userResponse.setCreated(userDB.getCreated());
            userResponse.setModified(userDB.getModified());
            userResponse.setLastLogin(userDB.getLastLogin());
            userResponse.setActive(userDB.isActive());
            userResponse.setName(userDB.getName());
            userResponse.setEmail(userDB.getEmail());
            userResponse.setPassword(userDB.getPassword());

            List<PhoneRequestDTO> phonesDB = new ArrayList<>();
            if (userDB.getPhones().size()!=0){
                userDB.getPhones().forEach(phone -> {
                    PhoneRequestDTO phoneRequestDB = new PhoneRequestDTO(phone.getNumber(),phone.getCitycode(), phone.getCountrycode());
                    phonesDB.add(phoneRequestDB);
                });
            }

            userResponse.setPhones(phonesDB);
            userResponse.setToken(token);

            userResponseOptional= Optional.of(userResponse);

            return userResponseOptional;

        }   else{
              userResponseOptional = Optional.empty();
              return userResponseOptional;
        }

    }
}
