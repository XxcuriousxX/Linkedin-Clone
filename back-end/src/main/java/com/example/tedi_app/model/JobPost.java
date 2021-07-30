package com.example.tedi_app.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class JobPost {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long jobPostId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "authorId", referencedColumnName = "userId")
    private User user;

    private String details;
    private String requiredSkills;

    // a list of keywords seperated by commas (ex. "software,kernel,unix,OS")
    private String keywords;
}
