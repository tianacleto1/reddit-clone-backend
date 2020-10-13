package com.anacleto.redditclonebackend.service;

import com.anacleto.redditclonebackend.model.Subreddit;
import com.anacleto.redditclonebackend.model.dto.SubredditDTO;
import com.anacleto.redditclonebackend.repository.SubredditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final AuthService authService;

    @Autowired
    public SubredditService(SubredditRepository subredditRepository, AuthService authService) {
        this.subredditRepository = subredditRepository;
        this.authService = authService;
    }

    @Transactional(readOnly = true)
    public List<SubredditDTO> getSubreddits() {
        return subredditRepository.findAll()
                                    .stream()
                                    .map(this::mapToDTO)
                                    .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SubredditDTO getById(Long id) {
        Subreddit subreddit = subredditRepository.findById(id).orElseThrow(/*() -> new
                SubredditNotFoundException("Subreddit not found with id " + id)*/);

        return mapToDTO(subreddit);
    }

    @Transactional
    public SubredditDTO createSubreddit(SubredditDTO subredditDTO) {
        Subreddit subreddit = subredditRepository.save(mapToSubreddit(subredditDTO));
        subredditDTO.setId(subreddit.getId());

        return subredditDTO;
    }

    private SubredditDTO mapToDTO(Subreddit subreddit) {
        return SubredditDTO.builder()
                            .id(subreddit.getId())
                            .name(subreddit.getName())
                            .description(subreddit.getDescription())
                            .postCount(subreddit.getPosts().size())
                            .build();
    }

    private Subreddit mapToSubreddit(SubredditDTO subredditDTO) {
        return Subreddit.builder().name(subredditDTO.getName())
                                    .description(subredditDTO.getDescription())
                                    .user(authService.getCurrentUser())
                                    .createdDate(Instant.now())
                                    .build();
    }
}
