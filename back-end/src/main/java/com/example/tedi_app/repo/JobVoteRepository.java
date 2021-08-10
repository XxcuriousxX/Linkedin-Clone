package com.example.tedi_app.repo;

import com.example.tedi_app.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface JobVoteRepository extends JpaRepository<JobVote, Long> {

    Optional<JobVote> findTopByJobPostAndUserOrderByVoteIdDesc(JobPost post, User currentUser);
    void deleteByVoteId(Long voteId);

    @Modifying
    @Query("delete from JobVote v where v.voteId=:id")
    void deleteVoteByMyID(@Param("id") Long id);

    Collection<JobVote> findAllByUser_UserId(Long id);
}