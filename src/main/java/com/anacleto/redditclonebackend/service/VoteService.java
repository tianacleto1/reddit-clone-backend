package com.anacleto.redditclonebackend.service;

import com.anacleto.redditclonebackend.exception.PostAlreadyVotedException;
import com.anacleto.redditclonebackend.model.Post;
import com.anacleto.redditclonebackend.model.Vote;
import com.anacleto.redditclonebackend.model.VoteType;
import com.anacleto.redditclonebackend.model.dto.VoteDTO;
import com.anacleto.redditclonebackend.repository.PostRepository;
import com.anacleto.redditclonebackend.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Autowired
    public VoteService(VoteRepository voteRepository, PostRepository postRepository, AuthService authService) {
        this.voteRepository = voteRepository;
        this.postRepository = postRepository;
        this.authService = authService;
    }

    @Transactional
    public void vote(VoteDTO voteDTO) {
        Post post = postRepository.findById(voteDTO.getPostId()).orElseThrow();

        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());

        if (voteByPostAndUser.isPresent() && voteByPostAndUser.get().getVoteType().equals(voteDTO.getVoteType())) {
            throw new PostAlreadyVotedException("You`ve already " + voteDTO.getVoteType() + "`d for this post");
        }

        if (VoteType.UPVOTE.equals(voteDTO.getVoteType())) {
            if (post.getVoteCount() == null) {
                post.setVoteCount(1);
            } else {
                post.setVoteCount(post.getVoteCount() + 1);
            }
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }

        voteRepository.save(mapToVote(voteDTO, post));
        postRepository.save(post);
    }

    private Vote mapToVote(VoteDTO voteDTO, Post post) {
        return Vote.builder()
                    .voteType(voteDTO.getVoteType())
                    .post(post)
                    .user(authService.getCurrentUser())
                    .build();
    }
}
