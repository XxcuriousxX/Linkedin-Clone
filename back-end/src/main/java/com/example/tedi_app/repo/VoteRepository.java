package com.example.tedi_app.repo;

import com.example.tedi_app.model.Post;
import com.example.tedi_app.model.PostViews;
import com.example.tedi_app.model.User;
import com.example.tedi_app.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
    void deleteByVoteId(Long voteId);

    @Modifying
    @Query("delete from Vote v where v.voteId=:id")
    void deleteVoteByMyID(@Param("id") Long id);


    Collection<Vote> findAllByUser_UserId(Long id);
}