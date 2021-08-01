package com.example.tedi_app.repo;

import com.example.tedi_app.model.Post;
import com.example.tedi_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser(User user); // returns all posts of user

    Post getByPostId(Long id);

}