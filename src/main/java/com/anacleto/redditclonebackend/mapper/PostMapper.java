package com.anacleto.redditclonebackend.mapper;

import com.anacleto.redditclonebackend.model.Post;
import com.anacleto.redditclonebackend.model.Subreddit;
import com.anacleto.redditclonebackend.model.User;
import com.anacleto.redditclonebackend.model.dto.PostRequestDTO;
import com.anacleto.redditclonebackend.model.dto.PostResponseDTO;
import com.anacleto.redditclonebackend.repository.CommentRepository;
import com.anacleto.redditclonebackend.repository.VoteRepository;
import com.anacleto.redditclonebackend.service.AuthService;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private AuthService authService;

    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "voteCount", constant = "0")
    public abstract Post map(PostRequestDTO postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    public abstract PostResponseDTO mapToDto(Post post);

    Integer commentCount(Post post) {
        if (commentRepository.findByPost(post) != null)
            return commentRepository.findByPost(post).size();
        else
            return 0;
    }

    String getDuration(Post post) {
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }
}
