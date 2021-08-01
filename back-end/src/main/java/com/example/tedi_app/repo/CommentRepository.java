package com.example.tedi_app.repo;

import com.example.tedi_app.model.Comment;
import com.example.tedi_app.model.Post;
import com.example.tedi_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
    List<Comment> findAllByUser(User user);


    Collection<Comment> findAllByUser_UserId(Long id);

}
