package com.example.tedi_app.model;


import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
@Setter
public class Personalinfo {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long infoId;


    private Long userId;



    private String work_desc;
    private String stud_desc;
    private String abilities_desc;

    public Personalinfo(Long userId, String work_desc, String stud_desc, String abilities_desc) {
        this.userId = userId;
        this.work_desc = work_desc;
        this.stud_desc = stud_desc;
        this.abilities_desc = abilities_desc;
    }


    public Personalinfo(Long userId) {
        // this.infoId = infoId;
        this.userId = userId;
        this.work_desc = "";
        this.stud_desc = "";
        this.abilities_desc = "";
    }
}
