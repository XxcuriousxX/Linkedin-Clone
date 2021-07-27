package com.example.tedi_app.service;

import com.example.tedi_app.dto.VoteData;
import com.example.tedi_app.exceptions.PostNotFoundException;
import com.example.tedi_app.exceptions.SpringTediException;
import com.example.tedi_app.model.Action;
import com.example.tedi_app.model.Post;
import com.example.tedi_app.model.Vote;
import com.example.tedi_app.repo.ActionsRepository;
import com.example.tedi_app.repo.PostRepository;
import com.example.tedi_app.repo.UserRepository;
import com.example.tedi_app.repo.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final ActionsRepository actionsRepository;

    @Transactional
    // returns false if it was already liked, so it will be unliked
    public boolean like(VoteData voteData) {

        Post post = postRepository.findById(voteData.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteData.getPostId()));
        System.out.println("Post id to be liked: " + post.getPostId());
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());

        if (voteByPostAndUser.isPresent()) { // if have already liked, then unlike
            Vote v = voteByPostAndUser.orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteData.getPostId()));
            post.setLikeCount(post.getLikeCount() - 1);
            System.out.println("Vote id to be deleted = " + v.getVoteId() + " \n\n");
            voteRepository.deleteVoteByMyID(v.getVoteId());
            postRepository.save(post);
            return false;
//            throw new SpringTediException("You have already liked this post");
        }
//        if (UPVOTE.equals(voteDto.getVoteType())) {
//            post.setVoteCount(post.getVoteCount() + 1);
//        } else {
//            post.setVoteCount(post.getVoteCount() - 1);
//        }

        Action a;
        post.setLikeCount(post.getLikeCount() + 1);
        actionsRepository.save(Action.new_like_action(post, authService.getCurrentUser(), post.getUser()));
        voteRepository.save(mapToVote(voteData, post));
        postRepository.save(post);
        return true;
    }

    public boolean has_liked(VoteData voteData) {
        Post post = postRepository.findById(voteData.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteData.getPostId()));
        System.out.println("Post id to be liked: " + post.getPostId());
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
        if (voteByPostAndUser.isPresent()) {
            return true;
        }
        else return false;
    }

    private Vote mapToVote(VoteData voteData, Post post) {
        return Vote.builder()  // change
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
