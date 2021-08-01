package com.example.tedi_app.model;

import com.example.tedi_app.model.JobPost;
import com.example.tedi_app.model.User;
import lombok.*;
import org.aspectj.weaver.Iterators;

import javax.persistence.*;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Setter

public class PostViews {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToOne(fetch = LAZY,cascade = {CascadeType.ALL})
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;


    @OneToOne(fetch = LAZY,cascade = {CascadeType.ALL})
    @JoinColumn(name = "postId", referencedColumnName = "postId")
    private Post post;


    int views = 0;

}
