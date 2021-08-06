package com.example.tedi_app.model;

import lombok.*;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long postId;

    @Nullable
    @Lob
    private String description;

    private Integer likeCount = 0;
    private Integer commentCount = 0;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;
    private Instant createdDate;
//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "id", referencedColumnName = "id")
//    private Subreddit subreddit;


}
