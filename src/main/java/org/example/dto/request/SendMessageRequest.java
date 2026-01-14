package org.example.dto.request;

import lombok.Data;
@Data
public class SendMessageRequest {
    private Integer conversationId;
    private String content;
    private Integer senderId;
}
