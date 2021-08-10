package com.example.tedi_app.controller;

import com.example.tedi_app.dto.VoteData;
import com.example.tedi_app.dto.VoteResponse;
import com.example.tedi_app.service.JobVoteService;
import com.example.tedi_app.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.status;


@RestController
@RequestMapping("/api/jobrequest/")
@AllArgsConstructor
public class JobRequestController {

    private final JobVoteService jobVoteService;

    @PostMapping
    public ResponseEntity<VoteResponse> jobrequest(@RequestBody VoteData voteData) {
        if (jobVoteService.like(voteData))
            return status(HttpStatus.OK).body(new VoteResponse(true));
        else
            return status(HttpStatus.OK).body(new VoteResponse(false));

    }
    @GetMapping("has_jobrequest/{id}")
    public ResponseEntity<VoteResponse> has_requested(@PathVariable Long id) {
        if (jobVoteService.has_liked(new VoteData(id)))
            return status(HttpStatus.OK).body(new VoteResponse(true));
        else return status(HttpStatus.OK).body(new VoteResponse(false));
    }

}