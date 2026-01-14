package org.example.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
public class MessageResponse {
    private Integer messageId;
    private Integer senderId;
    private String content;
    private Instant createAt;
}
