package com.rahul.instagram.notification;

import com.rahul.instagram.common.ApiResponse;
import com.rahul.instagram.notification.dto.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    private String getCurrentUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getMyNotifications(){
        String currentUserName = getCurrentUsername();

        List<NotificationResponse> notificationResponses = notificationService.getMyNotifications(currentUserName);

        ApiResponse<List<NotificationResponse>> response = ApiResponse.<List<NotificationResponse>>builder()
                .success(true)
                .message("Notifications fetched successfully.")
                .data(notificationResponses)
                .build();

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable Long notificationId){

        String currentUserName = getCurrentUsername();

        notificationService.markAsRead(notificationId, currentUserName);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Notification marked as read successfully.")
                .data(null)
                .build();

        return ResponseEntity.ok(response);
    }

}
