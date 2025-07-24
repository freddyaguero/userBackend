package com.userbackend.jwt.exception;

public class SignatureException extends RuntimeException{

    private final String errorMessage;
    
     public SignatureException(final String errorMessage){
        this.errorMessage= errorMessage;
     }

     public String getErrorMessage() {
         return errorMessage;
     }

}