package com.anacleto.redditclonebackend.controller;

import com.anacleto.redditclonebackend.model.dto.SubredditDTO;
import com.anacleto.redditclonebackend.service.SubredditService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public SubredditDTO getSubredditById(@PathVariable Long id) {
        return subredditService.getById(id);
    }

    @PostMapping
    public SubredditDTO createSubreddit(@RequestBody @Valid SubredditDTO subredditDTO) {
        return subredditService.createSubreddit(subredditDTO);
    }
}
