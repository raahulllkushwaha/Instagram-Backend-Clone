package com.rahul.instagram.feed;

import com.rahul.instagram.common.ApiResponse;
import com.rahul.instagram.post.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feed")
public class FeedController {
     private final FeedService feedService;

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostResponse>>> getFeed
            (@RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "10") int size){

        Pageable pageable = PageRequest.of(page, size);

        Page<PostResponse> feed = feedService.getFeed(getCurrentUsername(), pageable);

        ApiResponse<Page<PostResponse>> response = ApiResponse.<Page<PostResponse>>builder()
                .success(true)
                .message("Feed fetched successfully")
                .data(feed)
                .build();

        return ResponseEntity.ok(response);
    }

}
