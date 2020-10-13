package com.anacleto.redditclonebackend.mapper;

import com.anacleto.redditclonebackend.model.Comment;
import com.anacleto.redditclonebackend.model.Post;
import com.anacleto.redditclonebackend.model.User;
import com.anacleto.redditclonebackend.model.dto.CommentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "commentDTO.text")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "post", source = "post")
    Comment map(CommentDTO commentDTO, Post post, User user);

    @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
    @Mapping(target = "userName", expression = "java(comment.getUser().getUsername())")
    CommentDTO mapToDTO(Comment comment);
}
