package com.rahul.instagram.notification;

import com.rahul.instagram.common.exceptions.ResourceNotFoundException;
import com.rahul.instagram.notification.dto.NotificationResponse;
import com.rahul.instagram.user.User;
import com.rahul.instagram.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public List<NotificationResponse> getMyNotifications(String currentUsername){
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + currentUsername));

        List<Notification> notifications = notificationRepository.findByRecipientOrderByCreatedAtDesc(user);

        return notifications.stream()
                .map(notification -> NotificationResponse.builder()
                        .id(notification.getId())
                        .message(notification.getMessage())
                        .type(notification.getType())
                        .isRead(notification.isRead())
                        .createdAt(notification.getCreatedAt())
                        .build()
                ).toList();
    }

    public void markAsRead(Long notificationId, String currentUsername){
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));

        if(!notification.getRecipient().getUsername().equals(currentUsername)){
            throw new RuntimeException("ou are not allowed to update this notification");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
