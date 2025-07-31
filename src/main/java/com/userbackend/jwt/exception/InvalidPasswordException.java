package com.userbackend.jwt.exception;

public class InvalidPasswordException extends RuntimeException{

    private final String errorMessage;
    
     public InvalidPasswordException(final String errorMessage){
        this.errorMessage= errorMessage;
     }

     public String getErrorMessage() {
         return errorMessage;
     }

}