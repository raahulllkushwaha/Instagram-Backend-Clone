package com.rahul.instagram.follow;

import com.rahul.instagram.common.ApiResponse;
import com.rahul.instagram.common.exceptions.ResourceNotFoundException;
import com.rahul.instagram.user.User;
import com.rahul.instagram.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;
    private final UserRepository userRepository;

    private Long getCurrentUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return user.getId();
    }

    @PostMapping("/{targetUserId}")
    public ResponseEntity<ApiResponse<String>> follow(@PathVariable Long targetUserId) {
        Long currentUserId = getCurrentUserId();

        followService.followUser(currentUserId, targetUserId);

        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("Followed Successfully")
                .data(null)
                .build();
        return ResponseEntity.ok(response);

    }


    @DeleteMapping("/{targetUserId}")
    public ResponseEntity<ApiResponse<String>> unfollow(@PathVariable Long targetUserId) {
        Long currentUserId = getCurrentUserId();

        followService.unfollowUser(currentUserId, targetUserId);

        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("Successfully Unfollowed")
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }

        @GetMapping("/following/{userId}")
        public ResponseEntity<ApiResponse<List<UserNode>>> getFollowing (@PathVariable Long userId){
            List<UserNode> followingList = followService.getFollowing(userId);

            ApiResponse<List<UserNode>> response = ApiResponse.<List<UserNode>>builder()
                    .success(true)
                    .message("Following list fetched successfully")
                    .data(followingList)
                    .build();
            return ResponseEntity.ok(response);
        }

    @GetMapping("/followers/{userId}")
    public ResponseEntity<ApiResponse<List<UserNode>>> getFollowers(@PathVariable Long userId) {
        List<UserNode> followersList = followService.getFollowers(userId);

        ApiResponse<List<UserNode>> response = ApiResponse.<List<UserNode>>builder()
                .success(true)
                .message("Followers list fetched successfully")
                .data(followersList)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/mutual/{userId1}/{userId2}")
    public ResponseEntity<ApiResponse<List<UserNode>>> getMutual(@PathVariable Long userId1, @PathVariable Long userId2) {
        List<UserNode> mutualList = followService.getMutualFollowing(userId1, userId2);

        ApiResponse<List<UserNode>> response = ApiResponse.<List<UserNode>>builder()
                .success(true)
                .message("Mutual list fetched successfully")
                .data(mutualList)
                .build();
        return ResponseEntity.ok(response);
    }

}

