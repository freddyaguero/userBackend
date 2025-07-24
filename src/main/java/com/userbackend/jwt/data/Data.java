package com.userbackend.jwt.data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.userbackend.jwt.dto.PhoneRequestDTO;
import com.userbackend.jwt.dto.UserRequestDTO;
import com.userbackend.jwt.dto.UserResponseDTO;
import com.userbackend.jwt.entity.Phone;
import com.userbackend.jwt.entity.User;

public class Data {
    public  Optional<User> getUserOptional() {
        Optional<User>  userOptional= Optional.of(new User("prueba", "prueba@gmail.cl","12345AA"));
        return userOptional;
        
    }

     public  Optional<User> getUserOptionalEmpty() {
        Optional<User>  userOptional= Optional.empty();
        return userOptional;
        
    }

    public static UserRequestDTO getUserRequestDTO() {
        UserRequestDTO userRequestDTO = new UserRequestDTO("prueba", "prueba@gmail.cl", "12345");
        List<PhoneRequestDTO> phones = new ArrayList<>();
        PhoneRequestDTO phone = new PhoneRequestDTO(1112564563, 1, "56");
        phones.add(phone);

        userRequestDTO.setPhones(phones);

        return userRequestDTO;
        
    }

     public  User getUser() {
        User user = new User("prueba", "prueba@gmail.cl", "12345");
        return user;
        
    }

    
     public  User getUserDB() {
        User userDB = new User("prueba", "prueba@gmail.cl", "12345");
        
        userDB.setId(UUID.randomUUID());
        userDB.setCreated(LocalDateTime.now());
        userDB.setModified(LocalDateTime.now());
        userDB.setActive(true);

        List<Phone> phonesDB = new ArrayList<>();
        Phone phone = new  Phone(1112564563, 1, "56", userDB);
        phone.setId(UUID.randomUUID());
        phone.setUser(userDB);
        phonesDB.add(phone);

        userDB.setPhones(phonesDB);

        return userDB;
        
    }
    public static String getToken() {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjdXF1aUBnbWFpbC5jbCIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE3NTMxNDc5NDksImV4cCI6MTc1MzE0ODAwOX0.iA1LbzA3g5tWlZD40JFUF2_u-OEqzQtQ387IQDYRRagVSHNbXjpMcDDfGy_nO-FLDsYstJtxvlp4RZBJNZkYiQ";
        return token;
        
    }
    public  UserResponseDTO getUserResponse() {
        UserResponseDTO userResponse = new UserResponseDTO();
        userResponse.setId(UUID.randomUUID());
        userResponse.setCreated(LocalDateTime.now());
        userResponse.setModified(LocalDateTime.now());
         String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjdXF1aUBnbWFpbC5jbCIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE3NTMxNDc5NDksImV4cCI6MTc1MzE0ODAwOX0.iA1LbzA3g5tWlZD40JFUF2_u-OEqzQtQ387IQDYRRagVSHNbXjpMcDDfGy_nO-FLDsYstJtxvlp4RZBJNZkYiQ";
        userResponse.setToken(token);
        userResponse.setActive(true);
        userResponse.setName("prueba");
        userResponse.setEmail("prueba@gmail.com");
        userResponse.setPassword("12345");

        List<PhoneRequestDTO> phonesRequestDTO = new ArrayList<>();
        PhoneRequestDTO phoneRequestDTO = new  PhoneRequestDTO(1112564563, 1, "56");
    
        phonesRequestDTO.add(phoneRequestDTO);
        userResponse.setPhones(phonesRequestDTO);
   
        return userResponse;
        
    }
   
}
