package com.example.tedi_app.repo;

import com.example.tedi_app.model.JobPost;
import com.example.tedi_app.model.JobPostViews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobPostViewsRepository extends JpaRepository<JobPostViews,Long> {

    Optional<JobPostViews> findByUserUserId(Long id);
    Collection<JobPostViews> findAllByUser_UserId(Long id);
}