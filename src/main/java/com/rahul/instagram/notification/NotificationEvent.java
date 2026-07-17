package com.rahul.instagram.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {

    private Long recipientId; // whom we're seding noti
    private String senderUsername; // who perform the action (ex:- rahul liked....)
    private NotificationType type; // LIKE, COMMENT, FOLLOW
    private String message; // final text message
}
