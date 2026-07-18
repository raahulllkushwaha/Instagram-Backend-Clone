package com.rahul.instagram.chat.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MessageResponse {

    private Long id;
    private String senderUsername;
    private String receiverUsername;
    private String content;
    private LocalDateTime createdAt;
}
