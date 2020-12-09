package com.anacleto.redditclonebackend.service;

import com.anacleto.redditclonebackend.model.Post;
import com.anacleto.redditclonebackend.model.Subreddit;
import com.anacleto.redditclonebackend.model.dto.SubredditDTO;
import com.anacleto.redditclonebackend.repository.SubredditRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.aspectj.bridge.MessageUtil.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SubredditServiceTest {

    private final SubredditRepository mockSubredditRepository = mock(SubredditRepository.class);
    private final AuthService mockAuthService = mock(AuthService.class);
    private SubredditService subredditService;
    private Subreddit mockSubreddit;

    @BeforeEach
    public void setUp() {
        subredditService = new SubredditService(mockSubredditRepository, mockAuthService);
        mockSubreddit = createSubredditMock();
    }

    @Test
    public void whenGetSubreddits_itShouldReturnListOfSubredditsTest() {
        when(mockSubredditRepository.findAll()).thenReturn(singletonList(mockSubreddit));

        List<SubredditDTO> subreddits = subredditService.getSubreddits();

        assertEquals("Name Test", subreddits.get(0).getName());
        assertEquals(1L, subreddits.get(0).getPostCount().longValue());
    }

    @Test
    public void whenGetSubredditById_itShouldReturnTheSubredditTest() {
        when(mockSubredditRepository.findById(any())).thenThrow(NoSuchElementException.class);

        try {
            subredditService.getById(0L);
            fail("It should throw NoSuchElementException!");
        } catch (NoSuchElementException ex) {
            assertTrue(true);
        }
    }

    @Test
    public void whenGetSubredditByIdNotFound_itShouldThrowNoSuchElementExceptionTest() {
        when(mockSubredditRepository.findById(any())).thenReturn(Optional.of(mockSubreddit));

        SubredditDTO returned = subredditService.getById(1L);

        assertEquals(1L, returned.getId().longValue());
    }

    @Test
    public void whenCreateSubredditIsCalled_itShouldReturnTheSubredditCreatedTest() {
        when(mockSubredditRepository.save(any())).thenReturn(mockSubreddit);

        SubredditDTO created = subredditService.createSubreddit(SubredditDTO.builder().build());

        assertEquals( 1L, created.getId().longValue());
    }

    private Subreddit createSubredditMock() {
        return new Subreddit(1L,
                           "Name Test",
                       "Description Test",
                                 singletonList(new Post(2L,"Post Name", "", "description", 4, null, Instant.now(), null)),
                                 Instant.now(),
                                 null);
    }
}
