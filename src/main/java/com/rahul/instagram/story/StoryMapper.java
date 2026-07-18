package com.rahul.instagram.story;

import com.rahul.instagram.story.dto.StoryResponse;
import org.springframework.stereotype.Component;

@Component
public class StoryMapper {
    public StoryResponse toStoryResponse(Story story){
        return StoryResponse.builder()
                .id(story.getId())
                .mediaUrl(story.getMediaUrl())
                .username(story.getUser().getUsername())
                .userProfilePicUrl(story.getUser().getProfilePicUrl())
                .createdAt(story.getCreatedAt())
                .expiresAt(story.getExpiresAt())
                .build();
    }
}
