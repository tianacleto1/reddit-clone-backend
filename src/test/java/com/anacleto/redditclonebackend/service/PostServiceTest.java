package com.anacleto.redditclonebackend.service;

import com.anacleto.redditclonebackend.mapper.PostMapper;
import com.anacleto.redditclonebackend.model.Post;
import com.anacleto.redditclonebackend.model.Subreddit;
import com.anacleto.redditclonebackend.model.User;
import com.anacleto.redditclonebackend.model.dto.PostRequestDTO;
import com.anacleto.redditclonebackend.model.dto.PostResponseDTO;
import com.anacleto.redditclonebackend.repository.PostRepository;
import com.anacleto.redditclonebackend.repository.SubredditRepository;
import com.anacleto.redditclonebackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostServiceTest {

    private final PostRepository mockPostRepository = mock(PostRepository.class);
    private final SubredditRepository mockSubredditRepository = mock(SubredditRepository.class);
    private final UserRepository mockUserRepository = mock(UserRepository.class);
    private final PostMapper mockPostMapper = mock(PostMapper.class);
    private final AuthService mockAuthService = mock(AuthService.class);
    private Post mockPost;
    private PostService postService;

    @BeforeEach
    public void setUp() {
        postService = new PostService(mockPostRepository, mockSubredditRepository, mockUserRepository,
                                                                mockAuthService, mockPostMapper);
        mockPost = createMockPost();
    }

    @Test
    public void whenGetPostById_itShouldReturnThePostTest() {
        when(mockPostRepository.findById(any())).thenReturn(Optional.of(mockPost));
        when(mockPostMapper.mapToDto(any())).thenReturn(createMockPostResponse());

        PostResponseDTO response = postService.getPost(1L);

        assertEquals(1, response.getId().longValue());
        assertEquals("Post Name", response.getPostName());
    }

    @Test
    public void whenGetPostByIdNotFound_itShouldThrowNoSuchElementExceptionTest() {
        when(mockPostRepository.findById(any())).thenThrow(NoSuchElementException.class);

        try {
            postService.getPost(0L);
            fail("It should throw NoSuchElementException!");
        } catch (NoSuchElementException ex) {
            assertTrue(true);
        }
    }

    @Test
    public void whenGetAllPostsIsCalled_ItShouldReturnListOfPostsTest() {
        when(mockPostRepository.findAll()).thenReturn(singletonList(mockPost));
        when(mockPostMapper.mapToDto(any())).thenReturn(createMockPostResponse());

        List<PostResponseDTO> posts = postService.getAllPosts();

        assertEquals("Post Name", posts.get(0).getPostName());
    }

    @Test
    public void whenSave_ItShouldReturnSavedPostTest() {
        when(mockSubredditRepository.findByName(any())).thenReturn(Optional.of(createSubredditMock()));
        when(mockPostRepository.save(any())).thenReturn(createMockPost());
        when(mockPostMapper.mapToDto(any())).thenReturn(createMockPostResponse());
        when(mockAuthService.getCurrentUser()).thenReturn(createMockUser());

        PostResponseDTO returned = postService.save(createMockPostRequest());

        assertEquals("Post Name", returned.getPostName());
    }

    @Test
    public void whenGetPostsBySubreddit_itShouldReturnPostListTest() {
        when(mockSubredditRepository.findById(any())).thenReturn(Optional.of(createSubredditMock()));
        when(mockPostRepository.findAllBySubreddit(any())).thenReturn(Collections.singletonList(createMockPost()));
        when(mockPostMapper.mapToDto(any())).thenReturn(createMockPostResponse());

        List<PostResponseDTO> posts = postService.getPostsBySubreddit(1L);

        assertEquals("Post Name", posts.get(0).getPostName());
    }

    @Test
    public void whenGetPostsByUsername_itShouldReturnPostListTest() {
        when(mockUserRepository.findByUsername(any())).thenReturn(Optional.of(createMockUser()));
        when(mockPostRepository.findByUser(any())).thenReturn(Collections.singletonList(createMockPost()));
        when(mockPostMapper.mapToDto(any())).thenReturn(createMockPostResponse());

        List<PostResponseDTO> posts = postService.getPostsByUsername("Name");

        assertEquals("Post Name", posts.get(0).getPostName());
    }

    private Post createMockPost() {
        return new Post(1L, "Post Name", "url.com",
                "Description test", 2, createMockUser(), Instant.now(), createSubredditMock());
    }

    private PostResponseDTO createMockPostResponse() {
        return new PostResponseDTO(1L, "Post Name", "url.com", "Description test",
                 "userName", "Subreddit Name", 1, 1, "duration");
    }

    private PostRequestDTO createMockPostRequest() {
        return new PostRequestDTO(1L, "subredditName", "postName", "url.com", "description");
    }

    private Subreddit createSubredditMock() {
        return new Subreddit(1L,
                "postName",
                "Description Test",
                singletonList(new Post(2L,"subredditName", "", "description", 4, null, Instant.now(), null)),
                Instant.now(),
                null);
    }

    private User createMockUser() {
        return new User(2L, "Name", "pass", "name@gmail.com", Instant.now(), false);
    }
}
