package com.userbackend.jwt.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class UserRequestDTO {
    private String name;

    @NotNull
	private String email;
	
    @NotNull
    @Size(min=8,max=12)
	private String password;
    
    private List<PhoneRequestDTO> phones = new ArrayList<>();
   
    public UserRequestDTO(String name,  @NotNull String email,  @NotNull @Size(min = 8, max = 12) String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public UserRequestDTO(){
        
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<PhoneRequestDTO> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneRequestDTO> phones) {
        this.phones = phones;
    }



    
}
