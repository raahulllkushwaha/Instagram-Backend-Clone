package com.rahul.instagram.user;

import com.rahul.instagram.common.exceptions.ResourceNotFoundException;
import com.rahul.instagram.follow.FollowService;
import com.rahul.instagram.user.dto.UpdateProfileRequest;
import com.rahul.instagram.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final FollowService followService; // to find the count

    public UserResponse getProfile(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));

        int followerCount = followService.getFollowers(user.getId()).size();
        int followingCount = followService.getFollowing(user.getId()).size();

        UserResponse response = userMapper.toUserResponse(user, followerCount, followingCount);

        return response;
    }

    public UserResponse getMyProfile(String currentUsername) {
        return getProfile(currentUsername);
    }

    public UserResponse updateProfile(String currentUsername, UpdateProfileRequest request){
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getBio() != null){
            user.setBio(request.getBio());
        }

        if(request.getProfilePicUrl() != null){
            user.setProfilePicUrl(request.getProfilePicUrl());
        }

        User updatedUser = userRepository.save(user);

        int followerCount = followService.getFollowers(updatedUser.getId()).size();
        int followingCount = followService.getFollowing(updatedUser.getId()).size();

        return userMapper.toUserResponse(updatedUser, followerCount, followingCount);
    }


}
