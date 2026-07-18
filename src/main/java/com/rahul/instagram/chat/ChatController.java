package com.rahul.instagram.chat;

import com.rahul.instagram.chat.dto.MessageResponse;
import com.rahul.instagram.chat.dto.SendMessageRequest;
import com.rahul.instagram.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate; // for websocket

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping("/{otherUserId}")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getConversation(@PathVariable Long otherUserId) {
        List<MessageResponse> messages = chatService.getConversation(getCurrentUsername(), otherUserId);

        ApiResponse<List<MessageResponse>> response = ApiResponse.<List<MessageResponse>>builder()
                .success(true)
                .message("Conversation fetched successfully")
                .data(messages)
                .build();

        return ResponseEntity.ok(response);
    }

    // WebSocket handler: real-time message
    @MessageMapping("/chat.send")
    public void sendMessage(@Payload SendMessageRequest request, java.security.Principal principal){

        MessageResponse savedMessage = chatService.sendMessage(principal.getName(), request);

        // send receiver real time message
        messagingTemplate.convertAndSendToUser(
                savedMessage.getReceiverUsername(),
                "/queue/messages",
                savedMessage
        );
    }
}
