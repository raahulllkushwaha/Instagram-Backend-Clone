package com.rahul.instagram.user.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String bio;
    private String profilePicUrl;
    private LocalDateTime createdAt;
}
