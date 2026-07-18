package com.rahul.instagram.auth;

import com.rahul.instagram.auth.dto.AuthResponse;
import com.rahul.instagram.auth.dto.SignupRequest;
import com.rahul.instagram.user.User;
import com.rahul.instagram.user.UserMapper;
import com.rahul.instagram.user.UserRepository;
import com.rahul.instagram.user.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthService authService;

    @Test
    void signUp_shouldReturnAuthResponse_whenValidRequest() {
        SignupRequest request = new SignupRequest();
        request.setUsername("newuser");
        request.setEmail("newuser@test.com");
        request.setPassword("password123");

        User savedUser = User.builder().id(1L).username("newuser").build();
        UserResponse userResponse = UserResponse.builder().id(1L).username("newuser").build();

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("newuser@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtUtil.generateToken("newuser")).thenReturn("fake-jwt-token");
        when(userMapper.toUserResponse(savedUser, 0, 0)).thenReturn(userResponse);

        AuthResponse response = authService.signUp(request);

        assertNotNull(response);
        assertEquals("fake-jwt-token", response.getToken());
        assertEquals("newuser", response.getUser().getUsername());
    }
}