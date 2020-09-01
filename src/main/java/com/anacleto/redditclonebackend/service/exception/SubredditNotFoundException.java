package com.anacleto.redditclonebackend.service.exception;

public class SubredditNotFoundException extends RuntimeException {

    public SubredditNotFoundException(String s) {
        super(s);
    }
}
