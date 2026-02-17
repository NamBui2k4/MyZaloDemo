package org.example.dto.socket;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.entity.MessageStatus;

import java.time.Instant;

// DTO Client gửi lên khi nhận/xem tin
@Data
@AllArgsConstructor
public class MessageStatusDTO {
    private Integer messageId;
    private Integer senderId;
        private MessageStatus.MessageReceiptStatus status;
    private Instant receiverTime;
}