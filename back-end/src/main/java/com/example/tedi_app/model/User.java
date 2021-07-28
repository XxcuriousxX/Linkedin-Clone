package com.example.tedi_app.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
        @Id
        @GeneratedValue(strategy = IDENTITY)
        private Long userId;
        @NotBlank(message = "Username is required")
        private String username;
        @NotBlank(message = "Password is required")
        private String password;
        @Email
        @NotEmpty(message = "Email is required")
        @Column(nullable = false,unique = true)
        private String email;

        @NotBlank(message = "Phone is required")
        private String phone;
        @NotBlank(message = "First Name is required")
        private String first_name;
        @NotBlank(message = "Last Name is required")
        private String last_name;
        @NotBlank(message = "Company Name is required")
        private String company_name;

        private Instant created;
        private boolean enabled;


        @OneToOne(fetch = LAZY,cascade = {CascadeType.ALL})
        @JoinColumn(name = "personalInfoId", referencedColumnName = "infoId")
        @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
        public Personalinfo personalinfo;
}

