package com.userbackend.jwt.exception;

public class JwtException extends RuntimeException{

    private final String errorMessage;
    
     public JwtException(final String errorMessage){
        this.errorMessage= errorMessage;
     }

     public String getErrorMessage() {
         return errorMessage;
     }

}