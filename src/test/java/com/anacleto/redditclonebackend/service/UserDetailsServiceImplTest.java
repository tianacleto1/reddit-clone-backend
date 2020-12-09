package com.anacleto.redditclonebackend.service;

import com.anacleto.redditclonebackend.model.User;
import com.anacleto.redditclonebackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserDetailsServiceImplTest {

    private final UserRepository mockUserRepository = mock(UserRepository.class);
    private UserDetailsServiceImpl userDetailsService;
    private final User mockUser = createMockUser();

    @BeforeEach
    public void setUp() {
        userDetailsService = new UserDetailsServiceImpl(mockUserRepository);
    }

    @Test
    public void whenLoadUserByUsername_itShouldReturnUserDetailsTest() {
        when(mockUserRepository.findByUsername(any())).thenReturn(Optional.of(mockUser));

        UserDetails user = userDetailsService.loadUserByUsername("Name");

        assertEquals("Name", user.getUsername());
    }

    @Test
    public void whenLoadUserByUsernameNotFount_itShouldThrowUsernameNotFoundExceptionTest() {
        when(mockUserRepository.findByUsername(any())).thenReturn(Optional.empty());

        try {
            userDetailsService.loadUserByUsername("Name");
            fail("It should throw UsernameNotFoundException!");
        } catch (Exception ex) {
            assertTrue(ex instanceof UsernameNotFoundException);
            assertEquals("No user found with username : Name", ex.getMessage());
        }
    }

    private User createMockUser() {
        return new User(2L, "Name", "pass", "name@gmail.com", Instant.now(), false);
    }
}
