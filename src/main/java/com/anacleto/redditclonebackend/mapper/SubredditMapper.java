package com.anacleto.redditclonebackend.mapper;

import com.anacleto.redditclonebackend.model.Post;
import com.anacleto.redditclonebackend.model.Subreddit;
import com.anacleto.redditclonebackend.model.dto.SubredditDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubredditMapper {

    @Mapping(target = "postCount", expression = "java(mapPosts(subreddit.getPosts()))")
    SubredditDTO mapSubredditToDTO(Subreddit subreddit);

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    Subreddit mapDtoTOSubreddit(SubredditDTO subredditDTO);

    default Integer mapPosts(List<Post> numberOfPosts) {
        return numberOfPosts.size();
    }
}
