package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.CreatePrivateConversationRequest;
import org.example.dto.response.ConversationResponse;
import org.example.entity.Conversation;
import org.example.service.ConversationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conversations")
@RequiredArgsConstructor
public class ConversationController {
    private final ConversationService conversationService;

    @PostMapping("/private")
    public Conversation openOrCreatePrivateConversation(
            @RequestParam Integer currentUserId,
            @RequestBody CreatePrivateConversationRequest body
    ) {
        return conversationService.createOrGetPrivateConversation(
                currentUserId,
                body.getTargetUserId()
        );
    }

    @GetMapping
    public List<ConversationResponse> getConversationList (
            @RequestParam Integer currentUserId
    ){
        return conversationService.getAllConversation(currentUserId);
    }


}
