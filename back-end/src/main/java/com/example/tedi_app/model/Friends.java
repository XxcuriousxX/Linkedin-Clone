package com.example.tedi_app.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Friends {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @NotBlank
    private Long user_id1;
    @NotBlank
    private Long user_id2;

    private Boolean accepted;

    public Friends(Long user_id1, Long user_id2) {
        this.user_id1 = user_id1;
        this.user_id2 = user_id2;
        this.accepted = false;
    }
    public Boolean areConnected(){
        return this.accepted;
    }
}
