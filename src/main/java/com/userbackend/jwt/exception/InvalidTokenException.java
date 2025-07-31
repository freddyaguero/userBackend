package com.userbackend.jwt.exception;

public class InvalidTokenException extends RuntimeException{

    private final String errorMessage;
    
     public InvalidTokenException(final String errorMessage){
        this.errorMessage= errorMessage;
     }

     public String getErrorMessage() {
         return errorMessage;
     }

}
