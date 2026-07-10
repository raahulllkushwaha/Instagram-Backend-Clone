package com.rahul.instagram.user;

import com.rahul.instagram.user.dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toUserResponse(User user, int followersCount, int followingCount){
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .profilePicUrl(user.getProfilePicUrl())
                .createdAt(user.getCreatedAt())

                // new data
                .followersCount(followersCount)
                .followingCount(followingCount)

                .build();
    }
}
