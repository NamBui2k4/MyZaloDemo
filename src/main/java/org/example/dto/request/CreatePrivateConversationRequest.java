package org.example.dto.request;

import lombok.Data;

@Data
public class CreatePrivateConversationRequest {
    private Integer targetUserId;
}
