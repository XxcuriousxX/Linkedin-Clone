package com.example.tedi_app.model;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.time.Instant;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Setter
@Getter
public class Action {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long notificationId;


    @NotNull
    @ManyToOne(fetch = FetchType.LAZY) // a post has many notifications for a user
    @JoinColumn(name = "postId", referencedColumnName = "postId")
    private Post post;



    //
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "fromUserId", referencedColumnName = "userId")
    private User fromUser;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name ="toUserId", referencedColumnName = "userId")
    private User toUser;

    private Instant timeCreated = Instant.now();


//    @Nullable
//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "commentId", referencedColumnName = "commentId")
//    private Comment comment;

    // 0: a like action on post, 1: a comment notification on post
    private Integer actionType = -1;



    Action(Post post, User fromUser, User toUser) {
        this.post = post;
        this.fromUser = fromUser;
        this.toUser = toUser;
    }
    public static Action new_like_action(Post post, User fromUser, User toUser) {
        Action a = new Action(post, fromUser, toUser);
        a.actionType = 1;
        return a;
    }

//    public static Action new_comment_action(Post post, User fromUser, User toUser, Comment c) {
//        Action a = new Action(post, fromUser, toUser);
//        a.actionType = 2;
//        a.comment = c;
//        return a;
//    }

}
