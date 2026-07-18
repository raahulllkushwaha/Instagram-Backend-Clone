package com.rahul.instagram.like;

import com.rahul.instagram.post.Post;
import com.rahul.instagram.post.PostRepository;
import com.rahul.instagram.user.User;
import com.rahul.instagram.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LikeService likeService;

    @Test
    void likePost_shouldThrowException_whenAlreadyLiked() {
        // Arrange (fake data setup)
        User user = User.builder().id(1L).username("testuser").build();
        Post post = Post.builder().id(1L).build();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(likeRepository.existsByPostAndUser(post, user)).thenReturn(true);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            likeService.likePost("testuser", 1L);
        });
    }
}