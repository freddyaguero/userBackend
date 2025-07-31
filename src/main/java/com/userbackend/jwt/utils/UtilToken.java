package com.userbackend.jwt.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.json.JSONObject;

import com.userbackend.jwt.exception.InvalidTokenException;

public class UtilToken {

public static String getEmailFromToken(String authorizationHeader){

    String emailFromToken="";
    try {
        String tokenAuthorization = authorizationHeader.substring(7); 
                String[] parts = tokenAuthorization.split("\\.");
                if (parts.length == 3) {
                    byte[] payloadBytes = Base64.getUrlDecoder().decode(parts[1]);
                    String payload = new String(payloadBytes, StandardCharsets.UTF_8);
                    JSONObject jsonObject = new JSONObject(payload);
                    emailFromToken=jsonObject.getString("sub");
                }
            } catch (Exception e) {
                 throw new InvalidTokenException("Error al decodificar el token: " + e.getMessage());
            }

      return emailFromToken;

}

public static String getToken(String authorizationHeader){

    String tokenAuthorization="";
    try {
        tokenAuthorization = authorizationHeader.substring(7); 
                
        } catch (Exception e) {
                 throw new InvalidTokenException("Error al recuperar token: " + e.getMessage());
        }

      return tokenAuthorization;

}

}
