package org.example.dto.socket;

import lombok.Data;

// DTO Client gửi lên khi nhận/xem tin
@Data
public class MessageStatusUpdate {
    private Integer messageId;
    private Integer conversationId;
    private String status; // "DELIVERED" hoặc "SEEN"
}