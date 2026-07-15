package com.rahul.instagram.like;

import com.rahul.instagram.common.ApiResponse;
import com.rahul.instagram.post.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class LikeController {

    private final LikeService likeService;

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @PostMapping("/{postId}")
    public ResponseEntity<ApiResponse<String>> likePost(@PathVariable Long postId){
        likeService.likePost(getCurrentUsername(), postId);

        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("Post liked successfully")
                .data(null)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<String>> unlikePost(@PathVariable Long postId) {
        likeService.unlikePost(getCurrentUsername(), postId);

        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("Post unliked successfully")
                .data(null)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{postId}/count")
    public ResponseEntity<ApiResponse<Long>> getLikeCount(@PathVariable Long postId) {
       long count = likeService.getLikeCount(postId);

        ApiResponse<Long> response = ApiResponse.<Long>builder()
                .success(true)
                .message("Like count fetched successfully")
                .data(count)
                .build();

        return ResponseEntity.ok(response);

    }

    @GetMapping("/{postId}/status")
    public ResponseEntity<ApiResponse<Boolean>> isLikedByUser(@PathVariable Long postId) {
        boolean liked = likeService.isLikedByUser(getCurrentUsername(), postId);

        ApiResponse<Boolean> response = ApiResponse.<Boolean>builder()
                .success(true)
                .message("Like status fetched successfully")
                .data(liked)
                .build();

        return ResponseEntity.ok(response);
    }
}
