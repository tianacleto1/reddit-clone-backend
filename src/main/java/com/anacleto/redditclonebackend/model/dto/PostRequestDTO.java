package com.anacleto.redditclonebackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDTO {

    private Long postId;
    private String subredditName;
    private String postName;
    private String url;
    private String description;
}
