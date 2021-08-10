package com.example.tedi_app.mapper;


import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.example.tedi_app.dto.PostRequest;
import com.example.tedi_app.dto.PostResponse;
import com.example.tedi_app.model.*;
import com.example.tedi_app.repo.CommentRepository;
import com.example.tedi_app.repo.VoteRepository;
import com.example.tedi_app.service.AuthService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Optional;

//import static import com.example.tedi_app.model.VoteType.DOWNVOTE;
//import static import com.example.tedi_app.model.VoteType.UPVOTE;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;
//    @Autowired
//    private AuthService authService;

    // Mapping(target = "return field of map()", source = "argument's field")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "likeCount", constant = "0")
    @Mapping(target = "commentCount", constant = "0")
    @Mapping(target = "user", source = "user")
    public abstract Post map(PostRequest postRequest, User user);

    @Mapping(target = "postId", source = "postId")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    @Mapping(target = "createdDateLong", expression = "java(getCreatedDate(post))")
    public abstract PostResponse mapToDto(Post post);

    Integer commentCount(Post post) {
        return commentRepository.findByPost(post).size();
    }

    String getDuration(Post post) {
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }

    Long getCreatedDate(Post post) { return post.getCreatedDate().toEpochMilli(); }

//    boolean isPostUpVoted(Post post) {
//        return checkVoteType(post, UPVOTE);
//    }
//
//    boolean isPostDownVoted(Post post) {
//        return checkVoteType(post, DOWNVOTE);
//    }

//    private boolean checkVoteType(Post post) {
//        if (authService.isLoggedIn()){
//            Optional<Vote> voteForPostByUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post,
//                        authService.getCurrentUser());
//            return voteForPostByUser != null;
//        }
//        return false;
//    }

}