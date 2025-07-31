package com.userbackend.jwt.exception;

public class InvalidEmailException extends RuntimeException{

    private final String errorMessage;
    
     public InvalidEmailException(final String errorMessage){
        this.errorMessage= errorMessage;
     }

     public String getErrorMessage() {
         return errorMessage;
     }

}