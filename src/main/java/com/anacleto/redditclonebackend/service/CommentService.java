package com.anacleto.redditclonebackend.service;

import com.anacleto.redditclonebackend.mapper.CommentMapper;
import com.anacleto.redditclonebackend.model.Comment;
import com.anacleto.redditclonebackend.model.NotificationEmail;
import com.anacleto.redditclonebackend.model.Post;
import com.anacleto.redditclonebackend.model.User;
import com.anacleto.redditclonebackend.model.dto.CommentDTO;
import com.anacleto.redditclonebackend.repository.CommentRepository;
import com.anacleto.redditclonebackend.repository.PostRepository;
import com.anacleto.redditclonebackend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class CommentService {

    private static final String POST_URL = "";

    private final CommentMapper commentMapper;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final MailContentBuilderService mailContentBuilder;
    private final MailService mailService;

    @Autowired
    public CommentService(CommentMapper commentMapper, PostRepository postRepository, CommentRepository commentRepository,
                          UserRepository userRepository, AuthService authService,
                          MailContentBuilderService mailContentBuilder, MailService mailService) {
        this.commentMapper = commentMapper;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.authService = authService;
        this.mailContentBuilder = mailContentBuilder;
        this.mailService = mailService;
    }

    public void createComment(CommentDTO commentDTO) {
        Post post = postRepository.findById(commentDTO.getPostId()).orElseThrow();

        Comment comment = commentMapper.map(commentDTO, post, authService.getCurrentUser());
        commentRepository.save(comment);

        String message = mailContentBuilder.build(post.getUser().getUsername() + " posted a comment on your post." + POST_URL);
        sendCommentNotification(message, post.getUser());
    }

    public List<CommentDTO> getCommentsByPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();

        return commentRepository.findByPost(post)
                                .stream()
                                .map(commentMapper::mapToDTO)
                                .collect(Collectors.toList());

    }

    public List<CommentDTO> getCommentByUser(String userName) {
        User user = userRepository.findByUsername(userName).orElseThrow();

        return commentRepository.findAllByUser(user)
                                .stream()
                                .map(commentMapper::mapToDTO)
                                .collect(Collectors.toList());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(
                        user.getUsername() + " commented on your post ", user.getEmail(), message));
    }
}
