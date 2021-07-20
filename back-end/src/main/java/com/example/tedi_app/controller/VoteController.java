package com.example.tedi_app.controller;

import com.example.tedi_app.dto.VoteData;
import com.example.tedi_app.dto.VoteResponse;
import com.example.tedi_app.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/likes/")
@AllArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<VoteResponse> vote(@RequestBody VoteData voteData) {
        if (voteService.like(voteData))
            return status(HttpStatus.OK).body(new VoteResponse(true));
        else
            return status(HttpStatus.OK).body(new VoteResponse(false));

    }
    @GetMapping("has_liked/{id}")
    public ResponseEntity<VoteResponse> has_liked(@PathVariable Long id) {
        if (voteService.has_liked(new VoteData(id)))
            return status(HttpStatus.OK).body(new VoteResponse(true));
        else return status(HttpStatus.OK).body(new VoteResponse(false));
    }

}