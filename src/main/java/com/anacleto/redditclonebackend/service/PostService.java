package com.anacleto.redditclonebackend.service;

import com.anacleto.redditclonebackend.exception.PostNotFoundException;
import com.anacleto.redditclonebackend.mapper.PostMapper;
import com.anacleto.redditclonebackend.model.Post;
import com.anacleto.redditclonebackend.model.Subreddit;
import com.anacleto.redditclonebackend.model.User;
import com.anacleto.redditclonebackend.model.dto.PostRequestDTO;
import com.anacleto.redditclonebackend.model.dto.PostResponseDTO;
import com.anacleto.redditclonebackend.repository.PostRepository;
import com.anacleto.redditclonebackend.repository.SubredditRepository;
import com.anacleto.redditclonebackend.repository.UserRepository;
import com.anacleto.redditclonebackend.service.exception.SubredditNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostMapper postMapper;

    @Autowired
    public PostService(PostRepository postRepository,
                       SubredditRepository subredditRepository,
                       UserRepository userRepository,
                       AuthService authService,
                       PostMapper postMapper) {
        this.postRepository = postRepository;
        this.subredditRepository = subredditRepository;
        this.userRepository = userRepository;
        this.authService = authService;
        this.postMapper = postMapper;
    }

    @Transactional(readOnly = true)
    public PostResponseDTO getPost(Long id) {
        Post post = postRepository.findById(id)
                        .orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found!" ));

        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponseDTO> getAllPosts() {
        return postRepository.findAll()
                             .stream()
                             .map(postMapper::mapToDto)
                             .collect(Collectors.toList());
    }

    public void save(PostRequestDTO postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                                         .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
        postRepository.save(postMapper.map(postRequest, subreddit, authService.getCurrentUser()));
    }

    @Transactional(readOnly = true)
    public List<PostResponseDTO> getPostsBySubreddit(Long subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId)
                                    .orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);

        return posts.stream().map(postMapper::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponseDTO> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                            .orElseThrow(() -> new UsernameNotFoundException(username));

        return postRepository.findByUser(user)
                             .stream()
                             .map(postMapper::mapToDto)
                             .collect(Collectors.toList());
    }
}
