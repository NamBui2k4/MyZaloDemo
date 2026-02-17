package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConversationResponse {
    private Integer conversationId;
    private String userName;
    private String lastMessage;
    private String avatarUrl;
}
