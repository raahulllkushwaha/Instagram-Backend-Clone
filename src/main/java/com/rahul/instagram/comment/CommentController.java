package com.rahul.instagram.comment;

import com.rahul.instagram.comment.dto.CommentResponse;
import com.rahul.instagram.comment.dto.CreateCommentRequest;
import com.rahul.instagram.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @PostMapping("/{postId}")
    public ResponseEntity<ApiResponse<CommentResponse>> addComment(@PathVariable Long postId, @Valid @RequestBody CreateCommentRequest request){
        CommentResponse commentResponse = commentService.addComment(getCurrentUsername(), postId, request);

        ApiResponse<CommentResponse> response = ApiResponse.<CommentResponse>builder()
                .success(true)
                .message("Comment added successfully")
                .data(commentResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getComments(@PathVariable Long postId) {
        List<CommentResponse> comments = commentService.getCommentsByPost(postId);

        ApiResponse<List<CommentResponse>> response = ApiResponse.<List<CommentResponse>>builder()
                .success(true)
                .message("Comments fetched successfully")
                .data(comments)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<String>> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(getCurrentUsername(), commentId);

        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("Comment deleted successfully")
                .data(null)
                .build();

        return ResponseEntity.ok(response);
    }
}
