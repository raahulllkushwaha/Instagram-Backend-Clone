package com.rahul.instagram.user;

import com.rahul.instagram.common.ApiResponse;
import com.rahul.instagram.follow.UserNode;
import com.rahul.instagram.user.dto.UpdateProfileRequest;
import com.rahul.instagram.user.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    private String getCurrentUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping("/{username}")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile(@PathVariable String username){
        UserResponse userResponse = userService.getProfile(username);

        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .success(true)
                .message("Profile fetched successfully.")
                .data(userResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyProfile() {
        UserResponse userResponse = userService.getMyProfile(getCurrentUsername());

        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .success(true)
                .message("My profile fetched successfully.")
                .data(userResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateMyProfile(@Valid @RequestBody UpdateProfileRequest request) {

        UserResponse userResponse = userService.updateProfile(getCurrentUsername(), request);

        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .success(true)
                .message("My profile updated successfully.")
                .data(userResponse)
                .build();
        return ResponseEntity.ok(response);
    }
}
