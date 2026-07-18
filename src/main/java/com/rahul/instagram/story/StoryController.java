package com.rahul.instagram.story;

import com.rahul.instagram.common.ApiResponse;
import com.rahul.instagram.story.dto.StoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stories")
@RequiredArgsConstructor
public class StoryController {

    private final StoryService storyService;

    private String getCurrentUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<StoryResponse>> createStory(@RequestParam("file") MultipartFile file){

        StoryResponse storyResponse = storyService.createStory(getCurrentUsername(), file);

        ApiResponse<StoryResponse> response = ApiResponse.<StoryResponse>builder()
                .success(true)
                .message("Story created successfully!")
                .data(storyResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<ApiResponse<List<StoryResponse>>> getUserStories(@PathVariable String username){

        List<StoryResponse> stories = storyService.getActiveStoriesOfUser(username);

        ApiResponse<List<StoryResponse>> response = ApiResponse.<List<StoryResponse>>builder()
                .success(true)
                .message("User stories fetched successfully")
                .data(stories)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/feed")
    public ResponseEntity<ApiResponse<List<StoryResponse>>> getFeedStories() {

        List<StoryResponse> stories = storyService.getFeedStories(getCurrentUsername());

        ApiResponse<List<StoryResponse>> response = ApiResponse.<List<StoryResponse>>builder()
                .success(true)
                .message("Feed stories fetched successfully")
                .data(stories)
                .build();

        return ResponseEntity.ok(response);
    }
}
