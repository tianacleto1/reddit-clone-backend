package com.anacleto.redditclonebackend.controller;

import com.anacleto.redditclonebackend.model.dto.CommentDTO;
import com.anacleto.redditclonebackend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/v1/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<String> addComment(@RequestBody CommentDTO commentDTO) {
        commentService.createComment(commentDTO);

        return new ResponseEntity<>("Comment created successfully!", HttpStatus.CREATED);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDTO>> getAllCommentsByUser(@PathVariable("postId") Long postId) throws NoSuchElementException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentsByPost(postId));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userName}")
    public ResponseEntity<List<CommentDTO>> getAllCommentsByUser(@PathVariable("userName") String userName) throws NoSuchElementException{
        try {
            return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentByUser(userName));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
