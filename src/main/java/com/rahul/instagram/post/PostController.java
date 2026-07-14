package com.rahul.instagram.post;

import com.rahul.instagram.common.ApiResponse;
import com.rahul.instagram.media.MediaService;
import com.rahul.instagram.post.dto.CreatePostRequest;
import com.rahul.instagram.post.dto.PostResponse;
import com.rahul.instagram.user.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final MediaService mediaService;

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<PostResponse>> createPost(
            @RequestParam("caption") String caption,
            @RequestParam("file") MultipartFile file
    ){
        //step 1 upload file to cloudinary
        MediaService.UploadResult uploadResult = mediaService.uploadFile(file);

        //step2 generate CreatePostRequest object
        CreatePostRequest request = new CreatePostRequest();
        request.setCaption(caption);
        request.setMediaUrl(uploadResult.url());
        request.setMediaPublicId(uploadResult.publicId());


        PostResponse postResponse = postService.createPost(getCurrentUsername(), request);

        ApiResponse<PostResponse> response = ApiResponse.<PostResponse>builder()
                .success(true)
                .message("Post created successfully")
                .data(postResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> getPostById(@PathVariable Long postId, CreatePostRequest request){
        PostResponse postResponse = postService.getPostById(postId);

        ApiResponse<PostResponse> response = ApiResponse.<PostResponse>builder()
                .success(true)
                .message("My post fetched successfully!")
                .data(postResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getUserPosts(@PathVariable String username){

        List<PostResponse> posts = postService.getUserPosts(username);

        ApiResponse<List<PostResponse>> response = ApiResponse.<List<PostResponse>>builder()
                .success(true)
                .message("Users post fetched successfully!")
                .data(posts)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<String>> deletePost(@PathVariable Long postId){

        postService.deletePost(getCurrentUsername(), postId);

        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("Post deleted successfully!")
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }
}
