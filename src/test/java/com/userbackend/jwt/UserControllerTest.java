package com.userbackend.jwt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;


import com.userbackend.jwt.data.Data;
import com.userbackend.jwt.dto.UserResponseDTO;
import com.userbackend.jwt.entity.Phone;
import com.userbackend.jwt.entity.User;
import com.userbackend.jwt.repository.UserRepository;
import com.userbackend.jwt.services.UserServiceImpl;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest()
public class UserControllerTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

   
    
     

    @Test
	public void findByEmail() {
           Optional<User>  userOpt= Optional.of(new User("prueba", "prueba@gmail.cl","12345AA"));

           when(userRepository.findByEmail("prueba@gmail.com")).thenReturn(userOpt); 
           
           Optional<User> userOptional=   userService.findByEmail("prueba@gmail.com");

           assertEquals(true, userOptional.isPresent());
           assertEquals("12345AA", userOptional.get().getPassword());

           Optional<User>  userOptionalEmpty= Optional.empty();
           when(userRepository.findByEmail("noexiste@gmail.com")).thenReturn( userOptionalEmpty);
           userOptional=   userRepository.findByEmail("noexiste@gmail.com");
           assertEquals(false, userOptional.isPresent());

           verify(userRepository, times(1)).findByEmail("prueba@gmail.com");
           verify(userRepository, times(1)).findByEmail("noexiste@gmail.com");
           
	}


    @Test
	public void save() {

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


        when(userRepository.save(any(User.class))).thenReturn(userDB);
        UserResponseDTO saved = userService.save(Data.getUserRequestDTO(), Data.getToken());

        assertNotNull(saved);
        assertEquals("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjdXF1aUBnbWFpbC5jbCIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE3NTMxNDc5NDksImV4cCI6MTc1MzE0ODAwOX0.iA1LbzA3g5tWlZD40JFUF2_u-OEqzQtQ387IQDYRRagVSHNbXjpMcDDfGy_nO-FLDsYstJtxvlp4RZBJNZkYiQ"
           , saved.getToken());
        verify(userRepository, times(1)).save(any(User.class));
           
	}

    @Test
	public void saveWithoutNameAndPhones() {

        User userDB = new User(null, "prueba@gmail.cl", "12345");
        
        userDB.setId(UUID.randomUUID());
        userDB.setCreated(LocalDateTime.now());
        userDB.setModified(LocalDateTime.now());
        userDB.setActive(true);


        when(userRepository.save(any(User.class))).thenReturn(userDB);
        UserResponseDTO saved = userService.save(Data.getUserRequestDTO(), Data.getToken());

        assertNotNull(saved);
        assertEquals("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjdXF1aUBnbWFpbC5jbCIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE3NTMxNDc5NDksImV4cCI6MTc1MzE0ODAwOX0.iA1LbzA3g5tWlZD40JFUF2_u-OEqzQtQ387IQDYRRagVSHNbXjpMcDDfGy_nO-FLDsYstJtxvlp4RZBJNZkYiQ"
           , saved.getToken());
        verify(userRepository, times(1)).save(any(User.class));
   
	}


    @Test
    public void loadUserIsNotPresent() {
  
           User user= new User("prueba", "prueba@gmail.cl", "12345");
           Optional<User>  userOpt= Optional.of(user);

           lenient().when(userRepository.findByEmail("prueba@gmail.com")).thenReturn(userOpt); 
           
           Optional<UserResponseDTO> userResponseDTOOptional =userService.loadUser(Data.getUserRequestDTO(), Data.getToken());

           assertEquals(false, userResponseDTOOptional.isPresent());

          
    }
          
}
