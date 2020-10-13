package com.anacleto.redditclonebackend.controller;

import com.anacleto.redditclonebackend.model.dto.SubredditDTO;
import com.anacleto.redditclonebackend.service.SubredditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/subreddit")
public class SubredditController {

    private final SubredditService subredditService;

    @Autowired
    public SubredditController(SubredditService subredditService) {
        this.subredditService = subredditService;
    }

    @GetMapping
    public List<SubredditDTO> getAllSubreddits() {
        return subredditService.getSubreddits();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubredditDTO> getSubredditById(@PathVariable Long id) {
        return ResponseEntity.ok().body(subredditService.getById(id));
    }

    @PostMapping
    public ResponseEntity<SubredditDTO> createSubreddit(@RequestBody @Valid SubredditDTO subredditDTO) {
        SubredditDTO createdSubreddit = subredditService.createSubreddit(subredditDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdSubreddit);
    }
}
