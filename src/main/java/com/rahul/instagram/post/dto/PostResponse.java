package com.rahul.instagram.post.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostResponse {
    private Long id;
    private String caption;
    private String mediaUrl;
    private LocalDateTime createdAt;

    // to show some of owner data
    private String username;
    private String userProfilePicUrl;

    // To delete Cloudinary data
}
