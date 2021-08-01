package com.example.tedi_app.repo;

import com.example.tedi_app.model.PostViews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface PostViewsRepository extends JpaRepository<PostViews,Long> {

    Optional<PostViews> findByUserUserId(Long id);
    Collection<PostViews> findAllByUser_UserId(Long id);
}
