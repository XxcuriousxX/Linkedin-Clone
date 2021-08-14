package com.example.tedi_app.repo;

import com.example.tedi_app.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobVoteRepository extends JpaRepository<JobVote, Long> {

    Optional<JobVote> findTopByJobPostAndUserOrderByVoteIdDesc(JobPost post, User currentUser);
    void deleteByVoteId(Long voteId);

    @Query( value = "select user_id from job_vote  where job_post_id=:id", nativeQuery = true)
    List<Long> getUserIdForJobPost(@Param("id") Long id);

    @Modifying
    @Query("delete from JobVote v where v.voteId=:id")
    void deleteVoteByMyID(@Param("id") Long id);


    Collection<JobVote> findAllByUser_UserId(Long id);

    void deleteByJobPost_JobPostId(Long id);
}