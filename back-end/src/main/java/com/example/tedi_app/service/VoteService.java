package com.example.tedi_app.service;

import com.example.tedi_app.dto.VoteData;
import com.example.tedi_app.exceptions.PostNotFoundException;
import com.example.tedi_app.exceptions.SpringTediException;
import com.example.tedi_app.model.Post;
import com.example.tedi_app.model.Vote;
import com.example.tedi_app.repo.PostRepository;
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
    private final AuthService authService;

    @Transactional
    public void like(VoteData voteData) {

        Post post = postRepository.findById(voteData.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteData.getPostId()));
        System.out.println("Post id to be liked: " + post.getPostId());
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
        if (voteByPostAndUser.isPresent()) {
            throw new SpringTediException("You have already liked this post");
        }
//        if (UPVOTE.equals(voteDto.getVoteType())) {
//            post.setVoteCount(post.getVoteCount() + 1);
//        } else {
//            post.setVoteCount(post.getVoteCount() - 1);
//        }

        post.setLikeCount(post.getLikeCount() + 1);
        voteRepository.save(mapToVote(voteData, post));
        postRepository.save(post);
    }

    private Vote mapToVote(VoteData voteData, Post post) {
        return Vote.builder()  // change
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
