package com.example.tedi_app.repo;

import com.example.tedi_app.model.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost,Long> {


    Optional<JobPost> findByUserUserId(Long aLong);

}
