package com.userbackend.jwt.exception;

public class UserExistException extends RuntimeException{

    private final String errorMessage;
    
     public UserExistException(final String errorMessage){
        this.errorMessage= errorMessage;
     }

     public String getErrorMessage() {
         return errorMessage;
     }

}
