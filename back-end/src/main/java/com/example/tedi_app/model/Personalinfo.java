package com.example.tedi_app.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long infoId;
//    private Long userId;
    private String work_desc = "";
    private String stud_desc = "";
    private String abilities_desc = "";


    @OneToOne(fetch = LAZY,cascade = {CascadeType.ALL})
    @JoinColumn(name = "userId", referencedColumnName = "userId")
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    public Personalinfo(User u, String work_desc, String stud_desc, String abilities_desc) {
//        this.userId = userId;
        this.user = u;
        this.work_desc = work_desc;
        this.stud_desc = stud_desc;
        this.abilities_desc = abilities_desc;
    }

    public Personalinfo(User user) {
        this.user = user;
    }
}
