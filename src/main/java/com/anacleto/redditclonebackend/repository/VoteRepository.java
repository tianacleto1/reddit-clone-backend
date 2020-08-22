package com.anacleto.redditclonebackend.repository;

import com.anacleto.redditclonebackend.model.Post;
import com.anacleto.redditclonebackend.model.User;
import com.anacleto.redditclonebackend.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
