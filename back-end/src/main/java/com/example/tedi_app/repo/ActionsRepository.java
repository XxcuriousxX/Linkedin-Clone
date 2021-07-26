package com.example.tedi_app.repo;

import com.example.tedi_app.model.Action;
import com.example.tedi_app.model.Post;
import com.example.tedi_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActionsRepository extends JpaRepository<Action, Long> {
    List<Action> findByPost(Post post);

    Optional<List<Action>> findAllByToUserOrderByTimeCreatedAsc(User toUser);

}
