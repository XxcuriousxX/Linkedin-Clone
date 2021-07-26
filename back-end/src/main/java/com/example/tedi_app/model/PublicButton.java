package com.example.tedi_app.model;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@Entity
@Builder
@Getter
@Setter
public class PublicButton {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long publicbuttonId;

    private Long userId;
    private boolean work_exp;
    private boolean studies;
    private boolean abilities;
    private boolean company;
    private boolean phone;

    public PublicButton(Long userId,boolean work_exp, boolean studies, boolean abilities, boolean company, boolean phone) {
        this.userId = userId;
        this.work_exp = work_exp;
        this.studies = studies;
        this.abilities = abilities;
        this.company = company;
        this.phone = phone;
    }

    public PublicButton(Long userId) {
        this.userId = userId;
        this.work_exp = false;
        this.studies = true;
        this.abilities = true;
        this.company = false;
        this.phone = false;
    }
}
