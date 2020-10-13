package com.anacleto.redditclonebackend.exceptionhandler;

import com.anacleto.redditclonebackend.exception.FailToSendEmailException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler({ NoSuchElementException.class })
    public ResponseEntity<Object> handleNoSuchElementException() {
        return ResponseEntity.notFound().build();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({ FailToSendEmailException.class })
    public ResponseEntity<Object> handleNoSuchElementException(FailToSendEmailException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
