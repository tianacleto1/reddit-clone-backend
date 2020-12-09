package com.anacleto.redditclonebackend.service;

import com.anacleto.redditclonebackend.mapper.CommentMapper;
import com.anacleto.redditclonebackend.repository.CommentRepository;
import com.anacleto.redditclonebackend.repository.PostRepository;
import com.anacleto.redditclonebackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;

public class CommentServiceTest {

    private final CommentMapper mockCommentMapper = mock(CommentMapper.class);
    private final PostRepository mockPostRepository = mock(PostRepository.class);
    private final CommentRepository mockCommentRepository = mock(CommentRepository.class);
    private final UserRepository mockUserRepository = mock(UserRepository.class);
    private final AuthService mockAuthService = mock(AuthService.class);
    private final MailContentBuilderService mockMailContentBuilder = mock(MailContentBuilderService.class);
    private final MailService mockMailService = mock(MailService.class);

    private CommentService commentService;

    @BeforeEach
    public void setUp() {
        commentService = new CommentService(mockCommentMapper, mockPostRepository, mockCommentRepository,
                            mockUserRepository, mockAuthService, mockMailContentBuilder, mockMailService);
    }

    


}
