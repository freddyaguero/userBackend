package com.userbackend.jwt.exception;

public class UserNotExistException extends RuntimeException{

    private final String errorMessage;
    
     public UserNotExistException(final String errorMessage){
        this.errorMessage= errorMessage;
     }

     public String getErrorMessage() {
         return errorMessage;
     }

}