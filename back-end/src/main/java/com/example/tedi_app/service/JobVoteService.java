package com.example.tedi_app.service;

import com.example.tedi_app.dto.VoteData;
import com.example.tedi_app.exceptions.PostNotFoundException;
import com.example.tedi_app.model.*;
import com.example.tedi_app.repo.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class JobVoteService {

    private final JobVoteRepository jobVoteRepository;
    private final JobPostRepository jobPostRepository;
    private final UserRepository userRepository;
    private final AuthService authService;


    @Transactional
    // returns false if it was already liked, so it will be unliked
    public boolean like(VoteData voteData) {

        JobPost post = jobPostRepository.findById(voteData.getPostId())
                .orElseThrow(() -> new PostNotFoundException("JobPost Not Found with ID - " + voteData.getPostId()));
        System.out.println("JobPost id to be liked: " + post.getJobPostId());
        Optional<JobVote> jobvoteByPostAndUser = jobVoteRepository.findTopByJobPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());

        if (jobvoteByPostAndUser.isPresent()) { // if have already liked, then unlike
            JobVote v = jobvoteByPostAndUser.orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteData.getPostId()));
            post.setRequestedcount(post.getRequestedcount() - 1);
            System.out.println("Vote id to be deleted = " + v.getVoteId() + " \n\n");
            jobVoteRepository.deleteVoteByMyID(v.getVoteId());
            jobPostRepository.save(post);
            return false;
        }



        post.setRequestedcount(post.getRequestedcount() + 1);
        jobVoteRepository.save(mapToVote(voteData, post));
        jobPostRepository.save(post);
        return true;
    }

    public boolean has_liked(VoteData voteData) {
        JobPost post = jobPostRepository.findById(voteData.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteData.getPostId()));
        System.out.println("Post id to be liked: " + post.getJobPostId());
        Optional<JobVote> voteByPostAndUser = jobVoteRepository.findTopByJobPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
        if (voteByPostAndUser.isPresent()) {
            return true;
        }
        else return false;
    }

    private JobVote mapToVote(VoteData voteData, JobPost jobPost) {
        return JobVote.builder()  // change
                .jobPost(jobPost)
                .user(authService.getCurrentUser())
                .build();
    }
}
