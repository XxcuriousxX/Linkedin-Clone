package com.example.tedi_app.repo;

import com.example.tedi_app.model.JobPost;
import com.example.tedi_app.model.JobPostViews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobPostViewsRepository extends JpaRepository<JobPostViews,Long> {

    Optional<JobPostViews> findByUserUserId(Long id);
    JobPostViews getByJobPostJobPostIdAndUserUserId(Long jobpost_id, Long user_id);
    Collection<JobPostViews> findAllByUser_UserId(Long id);

    @Query( value = "select SUM(views) from job_post_views  where job_post_id=:id1 and user_id !=:id2 GROUP BY job_post_id", nativeQuery = true)
    List<Integer> getJobPostViews(@Param("id1") Long id1,@Param("id2") Long id2);

    void deleteByJobPost_JobPostId(Long id);

//    @Modifying
//    @Query("delete from job_post_views where job_post_id=:id")
//    void deleteJobPostViewsByJobPostId(@Param("id") Long id);



}