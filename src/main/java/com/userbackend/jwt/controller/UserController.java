package com.userbackend.jwt.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.userbackend.jwt.dto.UserError;
import com.userbackend.jwt.dto.UserFieldError;
import com.userbackend.jwt.dto.UserRequestDTO;
import com.userbackend.jwt.dto.UserResponseDTO;
import com.userbackend.jwt.services.UserService;
import com.userbackend.jwt.utils.UtilValidation;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Optional;
import com.userbackend.jwt.entity.User;
import com.userbackend.jwt.exception.UserExistException;
import com.userbackend.jwt.exception.UserNotExistException;

@RestController
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

            throw new UserExistException("email de usuario ya existe en la base de datos");
           
        }
		String token = getJWTToken(user.getEmail());
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user,token));
	}


	@PutMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody UserRequestDTO user) {

		String token = getJWTToken(user.getEmail());
         Optional<UserResponseDTO> userResponseOptional= userService.loadUser(user, token);
        if (!userResponseOptional.isPresent()) {
    
                 throw new UserNotExistException("email de usuario no existe en la base de datos");
        }

        return ResponseEntity.status(HttpStatus.FOUND).body(userResponseOptional.get());

    }
	

	private String getJWTToken(String username) {
		String secretKey = "mySecretKey";
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList("ROLE_USER");
		
		String token = Jwts
				.builder()
				.setSubject(username)
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 60000))//1 minuto
				.signWith(SignatureAlgorithm.HS512,
						secretKey.getBytes()).compact();

		return token;
	}
}
