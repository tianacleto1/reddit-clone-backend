package com.anacleto.redditclonebackend.exception;

public class PostAlreadyVotedException extends RuntimeException {

    public PostAlreadyVotedException(String message) {
        super(message);
    }
}
