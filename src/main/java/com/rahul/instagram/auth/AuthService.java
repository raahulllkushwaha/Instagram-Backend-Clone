package com.rahul.instagram.auth;

import com.rahul.instagram.auth.dto.AuthResponse;
import com.rahul.instagram.auth.dto.LoginRequest;
import com.rahul.instagram.auth.dto.SignupRequest;
import com.rahul.instagram.common.exceptions.InvalidCredentialsException;
import com.rahul.instagram.common.exceptions.ResourceNotFoundException;
import com.rahul.instagram.common.exceptions.UserAlreadyExistsException;
import com.rahul.instagram.user.User;
import com.rahul.instagram.user.UserMapper;
import com.rahul.instagram.user.UserRepository;

import com.rahul.instagram.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;

    public AuthResponse signUp(SignupRequest request){
        if(userRepository.existsByUsername(request.getUsername())){
            throw new UserAlreadyExistsException("...");
        }

        if(userRepository.existsByEmail(request.getEmail())){
            throw new UserAlreadyExistsException("...");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User savedUser = userRepository.save(user);


        String token = jwtUtil.generateToken(savedUser.getUsername());

        UserResponse userResponse = userMapper.toUserResponse(savedUser, 0, 0);

        return AuthResponse.builder()
                .token(token)
                .user(userResponse)
                .build();
    }



    public AuthResponse login(LoginRequest request){
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));

        boolean matches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!matches){
            throw new InvalidCredentialsException("Data is invalid");
        }

        String token = jwtUtil.generateToken(user.getUsername());

        UserResponse userResponse = userMapper.toUserResponse(user, 0, 0);

        return AuthResponse.builder()
                .token(token)
                .user(userResponse)
                .build();
    }
}
