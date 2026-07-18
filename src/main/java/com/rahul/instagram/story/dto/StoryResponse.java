package com.rahul.instagram.story.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class StoryResponse {
    private Long id;
    private String mediaUrl;
    private String username;
    private String userProfilePicUrl;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}
