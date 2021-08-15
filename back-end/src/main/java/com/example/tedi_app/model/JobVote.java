package com.example.tedi_app.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class JobVote {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long voteId;
    @NotNull
    @ManyToOne(fetch = LAZY,cascade = {CascadeType.ALL})
    @JoinColumn(name = "jobPostId", referencedColumnName = "jobPostId")
    private JobPost jobPost;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;
}

