package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "message_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MessageStatus {
    public enum MessageReceiptStatus{
        SENT, DELIVERED, SEEN, RECALL, CANNOT_SEND, DELETED
    }

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode // Bắt buộc để so sánh khóa
    public static class MessageStatusId implements Serializable {

        private Integer messageId; // Map với cột message_id
        private Integer userId;    // Map với cột user_id
    }

    @EmbeddedId
    private MessageStatusId statusId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    @MapsId("messageId")
    private Message message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @MapsId("userId")
    private User receiver;

    @Column(name = "status", nullable = false, columnDefinition = "varchar(10)")
    private MessageReceiptStatus status;
    // SENT, RECEIVED, SEEN

    @Column(name = "sent_time")
    private Instant sentTime;

    @Column(name = "recieve_time")
    private Instant receiveTime;

    @Column(name = "seen_time")
    private Instant seenTime;
}

