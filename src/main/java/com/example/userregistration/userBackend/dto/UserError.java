package com.example.userregistration.userBackend.dto;

import java.util.ArrayList;
import java.util.List;


public class UserError {
     
   List<UserFieldError> errors = new ArrayList<>();

   public List<UserFieldError> getErrors() {
    return errors;
   }

   public void setErrors(List<UserFieldError> errors) {
    this.errors = errors;
   }


   
}
