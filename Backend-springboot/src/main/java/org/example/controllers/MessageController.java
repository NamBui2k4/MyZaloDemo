package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.config.StompPrincipal;
import org.example.dto.request.CreateGroupRequest;
import org.example.dto.request.SendMessageRequest;
import org.example.dto.socket.MessageStatusDTO;
import org.example.entity.*;
import org.example.repository.UserRepository;
import org.example.service.ConversationService;
import org.example.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.example.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/conversations/{conversationId}/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ConversationService conversationService;
    private final UserRepository userRepository;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload SendMessageRequest dto, StompPrincipal principal) {
//        User currentUser = userRepository
//                .findByName(principal.getName())
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Integer senderId = 1;
        Integer conversationId = dto.getConversationId();
        String content = dto.getContent();

        Message message = messageService.sendMessage(
                conversationId,
                senderId,
                content
        );

        System.out.println(message.getContent());

        messagingTemplate.convertAndSend(
                "/topic/conversations/" + dto.getConversationId(),
                message
        );
    }

    @GetMapping
    public List<Message> getMessages(@PathVariable Integer conversationId) {
        return messageService.getMessages(conversationId);
    }


    @MessageMapping("/message.delivered")
    public void markAsDelivered(@Payload MessageStatusDTO payload, Principal principal) {

        Integer currentUserId = Integer.parseInt(principal.getName());

        System.out.println(currentUserId);

        // Gọi Service update DB
        MessageStatusDTO dto = messageService.markAsDelivered(payload.getMessageId(), currentUserId);

        // Báo lại cho Người Gửi (A) biết là B đã nhận
        // Gửi vào topic riêng của người gửi: /user/{senderId}/queue/status
        messagingTemplate.convertAndSendToUser(
                dto.getSenderId().toString(),
                "/queue/status",
                dto
        );
    }

    // 2. Client B gọi vào endpoint này khi mở tin nhắn ra xem
    // Client gửi tới: /app/message.seen
    @MessageMapping("/message.seen")
    public void markAsSeen(@Payload MessageStatusDTO payload, Principal principal) {
        Integer currentUserId = Integer.parseInt(principal.getName());

        MessageStatusDTO msDTO = messageService.markAsSeen(payload.getMessageId(), currentUserId);

        // Báo lại cho Người Gửi (A) biết là B đã xem
        messagingTemplate.convertAndSendToUser(
                msDTO.getStatus().toString(),
                "/queue/status",
                msDTO
        );
    }

    @PostMapping("/groups")
    public Group createGroup(
            @RequestParam Integer creatorId,
            @RequestBody CreateGroupRequest request
    ) {
        return messageService.createGroup(
                creatorId,
                request.getGroupName(),
                request.getMemberIds()
        );
    }

}
