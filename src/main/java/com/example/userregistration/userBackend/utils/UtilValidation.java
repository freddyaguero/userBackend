package com.example.userregistration.userBackend.utils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UtilValidation {

    public boolean isValidEmail(String email, String emailRegex) {
       
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean  isValidPass(String pass,String capitalRegex,String numbersRegex){

        boolean isValidPass =false;

        Pattern capitalPattern = Pattern.compile(capitalRegex);
        Matcher capitalMatcher = capitalPattern.matcher(pass);
        long countCapital = 0;
        countCapital = capitalMatcher.results().count();



        Pattern numbersPattern = Pattern.compile(numbersRegex);
        Matcher numbersMatcher = numbersPattern.matcher(pass);
        long countNumbers = 0;
        countNumbers = numbersMatcher.results().count();

     

        if (countCapital!=1){
           return isValidPass; 
        }
        if (countNumbers!=2){
            return isValidPass; 
        }
        return true;

    }

}
