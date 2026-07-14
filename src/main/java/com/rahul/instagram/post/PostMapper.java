package com.rahul.instagram.post;

import com.rahul.instagram.post.dto.PostResponse;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    public PostResponse toPostResponse(Post post){
        return PostResponse.builder()
                .id(post.getId())
                .caption(post.getCaption())
                .mediaUrl(post.getMediaUrl())
                .createdAt(post.getCreatedAt())
                .username(post.getUser().getUsername())
                .userProfilePicUrl(post.getUser().getProfilePicUrl())

                .build();
    }
}
