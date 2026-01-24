package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.CreateGroupRequest;
import org.example.dto.request.CreatePrivateConversationRequest;
import org.example.dto.request.SendMessageRequest;
import org.example.dto.socket.MessageStatusUpdate;
import org.example.entity.*;
import org.example.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.example.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/conversations")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload SendMessageRequest dto, Principal principal) {
        Integer senderId = Integer.parseInt(principal.getName());
        Integer conversationId = dto.getConversationId();
        String content = dto.getContent();

        Message message = chatService.sendMessage(
                conversationId,
                senderId,
                content
        );

        messagingTemplate.convertAndSend(
                "/topic/conversations/" + dto.getConversationId(),
                message
        );
    }

    @GetMapping("/{conversationId}/messages")
    public List<Message> getMessages(@PathVariable Integer conversationId) {
        return chatService.getMessages(conversationId);
    }

    @PostMapping("/private")
    public Conversation openPrivateConversation(
            @RequestParam Integer currentUserId,
            @RequestBody CreatePrivateConversationRequest request
    ) {
        return chatService.createOrGetPrivateConversation(
                currentUserId,
                request.getTargetUserId()
        );
    }

    @MessageMapping("/message.delivered")
    public void markAsDelivered(@Payload MessageStatusUpdate payload, Principal principal) {

        Integer currentUserId = Integer.parseInt(principal.getName());

        // Gọi Service update DB
        MessageStatus updatedStatus = chatService.markAsDelivered(payload.getMessageId(), currentUserId);

        // Báo lại cho Người Gửi (A) biết là B đã nhận
        // Gửi vào topic riêng của người gửi: /user/{senderId}/queue/status
        messagingTemplate.convertAndSendToUser(
                updatedStatus.getMessage().getSender().getUserId().toString(),
                "/queue/status",
                updatedStatus
        );
    }

    // 2. Client B gọi vào endpoint này khi mở tin nhắn ra xem
    // Client gửi tới: /app/message.seen
    @MessageMapping("/message.seen")
    public void markAsSeen(@Payload MessageStatusUpdate payload, Principal principal) {
        Integer currentUserId = Integer.parseInt(principal.getName());

        MessageStatus updatedStatus = chatService.markAsSeen(payload.getMessageId(), currentUserId);

        // Báo lại cho Người Gửi (A) biết là B đã xem
        messagingTemplate.convertAndSendToUser(
                updatedStatus.getMessage().getSender().getUserId().toString(),
                "/queue/status",
                updatedStatus
        );
    }

    @PostMapping("/groups")
    public Group createGroup(
            @RequestParam Integer creatorId,
            @RequestBody CreateGroupRequest request
    ) {
        return chatService.createGroup(
                creatorId,
                request.getGroupName(),
                request.getMemberIds()
        );
    }

}
