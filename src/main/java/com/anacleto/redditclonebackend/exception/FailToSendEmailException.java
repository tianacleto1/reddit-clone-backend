package com.anacleto.redditclonebackend.exception;

public class FailToSendEmailException extends RuntimeException {

    public FailToSendEmailException(String message) {
        super(message);
    }
}
