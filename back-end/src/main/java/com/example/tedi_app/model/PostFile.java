package com.example.tedi_app.model;


import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostFile {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long postfileId;

    @OneToOne(fetch = LAZY,cascade = {CascadeType.ALL})
    @JoinColumn(name = "postId", referencedColumnName = "postId")
    private Post post;

    @Nullable
    private String image;

    @Nullable
    private String video;

    @Nullable
    private String audio;


}
