package com.anacleto.redditclonebackend.model.dto;

import com.anacleto.redditclonebackend.model.VoteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteDTO {

    private VoteType voteType;
    private Long postId;
}
