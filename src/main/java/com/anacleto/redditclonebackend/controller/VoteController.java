package com.anacleto.redditclonebackend.controller;

import com.anacleto.redditclonebackend.model.dto.VoteDTO;
import com.anacleto.redditclonebackend.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/votes")
public class VoteController {

    private final VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping
    public ResponseEntity<VoteDTO> vote(@RequestBody VoteDTO voteDTO) {
        voteService.vote(voteDTO);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
