package com.anacleto.redditclonebackend.repository;

import com.anacleto.redditclonebackend.model.Comment;
import com.anacleto.redditclonebackend.model.Post;
import com.anacleto.redditclonebackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost(Post post);
    List<Comment> findAllByUser(User user);
}
