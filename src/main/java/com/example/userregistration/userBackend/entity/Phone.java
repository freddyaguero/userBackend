package com.example.userregistration.userBackend.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tbl_phones")
public class Phone {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private long number;
	private int citycode;
	private String countrycode;

 
    public Phone() {
    }

    public Phone(long number, int citycode, String countrycode, User user) {
        this.number = number;
        this.citycode = citycode;
        this.countrycode = countrycode;
        this.user = user;
    }

    public long getNumber() {
        return number;
    }
    public void setNumber(long number) {
        this.number = number;
    }
    public int getCitycode() {
        return citycode;
    }
    public void setCitycode(int citycode) {
        this.citycode = citycode;
    }
    public String getCountrycode() {
        return countrycode;
    }
    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    @ManyToOne
    @JsonIgnore
    private User user;

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }


   
}
