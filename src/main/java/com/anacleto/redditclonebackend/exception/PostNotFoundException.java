package com.anacleto.redditclonebackend.exception;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(String msg) {
        super(msg);
    }
}
