package com.example.tedi_app.controller;

import com.example.tedi_app.dto.JobPostRequest;
import com.example.tedi_app.dto.JobPostResponse;
import com.example.tedi_app.model.JobPost;

import com.example.tedi_app.service.JobPostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/jobs/")
@AllArgsConstructor
public class JobsController {

    private final JobPostService jobPostService;

    @GetMapping("suggestions/{username}")
    public ResponseEntity<List<JobPostResponse>> getSuggestions(@PathVariable String username) {
        System.out.println("SUggestions!!!");
        return status(HttpStatus.OK).body(jobPostService.getSuggestions(username));
    }


    @PostMapping("create/")
    public ResponseEntity<String> createJobPost(@RequestBody JobPostRequest jobPostRequest) {
        
        jobPostService.createJobPost(jobPostRequest);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
