package com.userbackend.jwt.controller;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.userbackend.jwt.dto.UserError;
import com.userbackend.jwt.dto.UserFieldError;
import com.userbackend.jwt.exception.JwtException;
import com.userbackend.jwt.exception.SignatureException;
import com.userbackend.jwt.exception.UnauthorizedException;
import com.userbackend.jwt.exception.UserExistException;
import com.userbackend.jwt.exception.UserNotExistException;

import io.jsonwebtoken.MalformedJwtException;

@ControllerAdvice
public class ExceptionController {


    @ExceptionHandler(value={UnauthorizedException.class})
    public ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException e){
         UserError userError= new UserError();
         List<UserFieldError> errors = new ArrayList<>();
        
                UserFieldError userFieldError = new UserFieldError();
                userFieldError.setTimestamp(LocalDateTime.now());
                userFieldError.setDetail(e.getErrorMessage());
                 userFieldError.setCodigo(403);
                errors.add(userFieldError);
  
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userError);
        
    }
    @ExceptionHandler(value={Exception.class})
    public ResponseEntity<Object> handleException(Exception e){
         UserError userError= new UserError();
         List<UserFieldError> errors = new ArrayList<>();
        
                UserFieldError userFieldError = new UserFieldError();
                userFieldError.setTimestamp(LocalDateTime.now());
                userFieldError.setDetail(e.getMessage());
                userFieldError.setCodigo(400);
                errors.add(userFieldError);
        userError.setErrors(errors); 
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userError);
        
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
         UserError userError= new UserError();
         List<UserFieldError> errors = new ArrayList<>();
         UserFieldError userFieldError = new UserFieldError();
                userFieldError.setTimestamp(LocalDateTime.now());
                userFieldError.setDetail("Acceso denegado: " + ex.getMessage());
                userFieldError.setCodigo(403);
                errors.add(userFieldError);
                userError.setErrors(errors); 
         return ResponseEntity.status(HttpStatus.FORBIDDEN).body(userError);
    }

    

    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<Object> handleUserExistException(UserExistException ex) {
         UserError userError= new UserError();
         List<UserFieldError> errors = new ArrayList<>();
         UserFieldError userFieldError = new UserFieldError();
                userFieldError.setTimestamp(LocalDateTime.now());
                userFieldError.setDetail (ex.getErrorMessage());
                userFieldError.setCodigo(400);
                errors.add(userFieldError);
                userError.setErrors(errors); 
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userError);
    }

    
    @ExceptionHandler(UserNotExistException.class)
    public ResponseEntity<Object> handleUserNotExistException(UserNotExistException ex) {
         UserError userError= new UserError();
         List<UserFieldError> errors = new ArrayList<>();
         UserFieldError userFieldError = new UserFieldError();
                userFieldError.setTimestamp(LocalDateTime.now());
                userFieldError.setDetail (ex.getErrorMessage());
                userFieldError.setCodigo(404);
                errors.add(userFieldError);
                userError.setErrors(errors); 
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userError);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Object> handleJwtException(JwtException ex) {
         UserError userError= new UserError();
         List<UserFieldError> errors = new ArrayList<>();
         UserFieldError userFieldError = new UserFieldError();
                userFieldError.setTimestamp(LocalDateTime.now());
                userFieldError.setDetail (ex.getErrorMessage());
                userFieldError.setCodigo(400);
                errors.add(userFieldError);
                userError.setErrors(errors); 
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userError);
    }


@ExceptionHandler(io.jsonwebtoken.SignatureException.class)
    public ResponseEntity<Object> handleSignatureException(SignatureException ex) {
         UserError userError= new UserError();
         List<UserFieldError> errors = new ArrayList<>();
         UserFieldError userFieldError = new UserFieldError();
                userFieldError.setTimestamp(LocalDateTime.now());
                userFieldError.setDetail (ex.getErrorMessage());
                userFieldError.setCodigo(400);
                errors.add(userFieldError);
                userError.setErrors(errors); 
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userError);
    }


    
    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<Object> handleMalformedJwtException(MalformedJwtException ex) {
         UserError userError= new UserError();
         List<UserFieldError> errors = new ArrayList<>();
         UserFieldError userFieldError = new UserFieldError();
                userFieldError.setTimestamp(LocalDateTime.now());
                userFieldError.setDetail (ex.getMessage());
                userFieldError.setCodigo(400);
                errors.add(userFieldError);
                userError.setErrors(errors); 
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userError);
    }
}
