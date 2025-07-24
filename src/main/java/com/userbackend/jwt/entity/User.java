package com.userbackend.jwt.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "tbl_users")
public class User {
	@Id
	@GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
	private UUID id;
	
	private String name;
	
	@NotBlank
    @Column(unique = true)
	private String email;
	
    @NotBlank
    @Size(min=8,max=12)
	private String password;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime created;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime modified;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime lastLogin;

    private boolean isActive;

    public LocalDateTime getCreated() {
        return created;
    }



    public void setCreated(LocalDateTime created) {
        this.created = created;
    }



    public LocalDateTime getModified() {
        return modified;
    }



    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }



    public LocalDateTime getLastLogin() {
        return lastLogin;
    }



    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }




    public boolean isActive() {
        return isActive;
    }



    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }



    public User() {
    }

    

    public User(String name, @NotBlank String email, @NotBlank @Size(min = 8, max = 12) String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
    

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Phone> phones = new ArrayList<>();


    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

}
