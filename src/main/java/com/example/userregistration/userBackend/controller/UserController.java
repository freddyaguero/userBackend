package com.example.userregistration.userBackend.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userregistration.userBackend.dto.UserError;
import com.example.userregistration.userBackend.dto.UserFieldError;
import com.example.userregistration.userBackend.dto.UserRequestDTO;
import com.example.userregistration.userBackend.entity.User;
import com.example.userregistration.userBackend.services.UserService;
import com.example.userregistration.userBackend.utils.UtilValidation;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${capital.regex}")
    private String capitalRegex;

    @Value("${numbers.regex}")
    private String numbersRegex;

    @Value("${email.regex1}")
    private String emailRegex1;

    @Value("${email.regex2}")
    private String emailRegex2;

    
   

    @PostMapping("/sign-up")
	public ResponseEntity<?> createUser(@Valid @RequestBody UserRequestDTO user, BindingResult result) {
        
 
        LocalDateTime now = LocalDateTime.now();
        UserError userError= new UserError();
        List<UserFieldError> errors = new ArrayList<>();

        UtilValidation util = new UtilValidation();
        boolean isValidPass=util.isValidPass(user.getPassword(), capitalRegex,numbersRegex );
        Boolean isValidEmail=util.isValidEmail(user.getEmail(),emailRegex1+emailRegex2);

        if(result.hasErrors()||!isValidPass||!isValidEmail){
            
            if(!isValidPass) {
                String fieldErrors ="password";
                String messageErrors = "debe tener una Mayúscula y dos números"; 
                UserFieldError userFieldError = new UserFieldError();
                userFieldError.setTimestamp(now);
                userFieldError.setDetail(fieldErrors+" "+messageErrors);
                 userFieldError.setCodigo(1);
                errors.add(userFieldError);
                
            }  
             if(!isValidEmail) {
                String fieldErrors ="email";
                String messageErrors = "el formato no es válido";  
                UserFieldError userFieldError = new UserFieldError();
                userFieldError.setTimestamp(now);
                userFieldError.setDetail(fieldErrors+" "+messageErrors);
                 userFieldError.setCodigo(2);
                errors.add(userFieldError);
            } 
            for (ObjectError error : result.getAllErrors()) {
                int fieldCode = ((FieldError) error).hashCode();
                
                String fieldErrors = ((FieldError) error).getField();
                String messageErrors = ((FieldError) error).getDefaultMessage();
                UserFieldError userFieldError = new UserFieldError();
                userFieldError.setTimestamp(now);
                userFieldError.setDetail(fieldErrors+" "+messageErrors);
                userFieldError.setCodigo(fieldCode);
                errors.add(userFieldError);
                
            }
           userError.setErrors(errors); 
           return ResponseEntity.badRequest().body(userError);
            
        }

        Optional<User> userOptional= userService.findByEmail(user.getEmail());

        if (userOptional.isPresent()) {
            String fieldErrors ="email";
            String messageErrors = "de usuario ya existe en la base de datos";  
            UserFieldError userFieldError = new UserFieldError();
            userFieldError.setTimestamp(now);
            userFieldError.setDetail(fieldErrors+" "+messageErrors);
            userFieldError.setCodigo(3);
            errors.add(userFieldError);
            userError.setErrors(errors); 
            return ResponseEntity.badRequest().body(userError);
        }

		return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
	}




    @GetMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody UserRequestDTO user) {
        LocalDateTime now = LocalDateTime.now();
        UserError userError= new UserError();
        List<UserFieldError> errors = new ArrayList<>();

        Optional<User> userOptional= userService.findByEmail(user.getEmail());
        if (!userOptional.isPresent()) {
                String fieldErrors ="email";
                String messageErrors = "de usuario no existe en la base de datos";  
                UserFieldError userFieldError = new UserFieldError();
                userFieldError.setTimestamp(now);
                userFieldError.setDetail(fieldErrors+" "+messageErrors);
                userFieldError.setCodigo(4);
                errors.add(userFieldError);
                userError.setErrors(errors); 
                return ResponseEntity.badRequest().body(userError);
        }

        return ResponseEntity.status(HttpStatus.FOUND).body(userOptional.get());

    }

}
