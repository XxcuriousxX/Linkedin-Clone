package com.example.tedi_app.model;

import javax.persistence.*;
import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
public class user implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,updatable = false)
    private Long id;

    private String first_name;
    private String last_name;
    private  String phone;
    private  String company_name;

    
    // @NotEmpty(message = "Email can not be empty")
    // @Email(message = "Please provide a valid email id")
    // @Column(name = "email", nullable = false, unique = true)
    private  String email;
//    where image is stored in our server
    private String image_path;
    private String password;

    public user(Long id, String username, String first_name, String last_name, String phone, String company_name, String email, String image_path) {
        this.id = id;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.company_name = company_name;
        this.email = email;
        this.image_path = image_path;
    }

    public user() {

    }

//    GETTERS BITCH
    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPhone() {
        return phone;
    }

    public String getCompany_name() {
        return company_name;
    }

    public String getEmail() {
        return email;
    }

    public String getImage_path() {
        return image_path;
    }


//    SETTERS BITCH


    public void setId(Long id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    @Override
    public String toString() {
        return "user{" +
                "id=" + id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", phone='" + phone + '\'' +
                ", company_name='" + company_name + '\'' +
                ", email='" + email + '\'' +
                ", image_path='" + image_path + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
