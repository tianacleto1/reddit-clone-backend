package com.anacleto.redditclonebackend.controller;

import com.anacleto.redditclonebackend.model.dto.PostRequestDTO;
import com.anacleto.redditclonebackend.model.dto.PostResponseDTO;
import com.anacleto.redditclonebackend.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> addPost(@RequestBody PostRequestDTO postRequestDTO) {
        PostResponseDTO savedPost = postService.save(postRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
    }

    @GetMapping
    public List<PostResponseDTO> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @GetMapping("/subreddit/{subredditId}")
    public ResponseEntity<List<PostResponseDTO>> getPostBySubreddit(@PathVariable Long subredditId) {
        return ResponseEntity.ok(postService.getPostsBySubreddit(subredditId));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<PostResponseDTO>> getPostsByUsername(@PathVariable String username) {
        return ResponseEntity.ok(postService.getPostsByUsername(username));
    }
}
