package com.rahul.instagram.chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SendMessageRequest {

    @NotBlank
    private String content;

    @NotNull
    private Long receiverId;
}
