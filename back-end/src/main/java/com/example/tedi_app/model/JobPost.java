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


    private String title;
    private String location;
    private String employmentType; // full-time or part-time, temporary


    private String details;
    private String requiredSkills;



    // a list of keywords seperated by commas (ex. "software,kernel,unix,OS")
    private String keywords;


    public JobPost(User u, String title, String location, String employmentType, String details
                                    , String requiredSkills, String keywords) {
        this.user = u;
        this.title = title;
        this.location = location;
        this.employmentType = employmentType;
        this.details = details;
        this.requiredSkills = requiredSkills;
        this.keywords = keywords;
    }
}
