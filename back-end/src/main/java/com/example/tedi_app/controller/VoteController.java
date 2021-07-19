package com.example.tedi_app.controller;

import com.example.tedi_app.dto.VoteData;
import com.example.tedi_app.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes/")
@AllArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<Void> vote(@RequestBody VoteData voteData) {
        voteService.like(voteData);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("has_liked/{id}")
    public ResponseEntity<Void> has_liked(@PathVariable Long id) {
        if (voteService.has_liked(new VoteData(id)) == false)
            return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}