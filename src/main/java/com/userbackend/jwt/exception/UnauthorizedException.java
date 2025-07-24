package com.userbackend.jwt.exception;

public class UnauthorizedException extends RuntimeException{

private final String errorMessage;
    
     public UnauthorizedException(final String errorMessage){
        this.errorMessage= errorMessage;
     }

     public String getErrorMessage() {
         return errorMessage;
     }

}
