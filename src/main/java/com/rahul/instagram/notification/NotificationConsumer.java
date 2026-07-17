package com.rahul.instagram.notification;

import com.rahul.instagram.user.User;
import com.rahul.instagram.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @KafkaListener(topics = "notification-events", groupId = "instaclone-group")
    public void consumeNotification(NotificationEvent event) {

        User recipient = userRepository.findById(event.getRecipientId())
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        Notification notification = Notification.builder()
                .recipient(recipient)
                .message(event.getMessage())
                .type(event.getType())
                .isRead(false)
                .build();

        notificationRepository.save(notification);
    }
}