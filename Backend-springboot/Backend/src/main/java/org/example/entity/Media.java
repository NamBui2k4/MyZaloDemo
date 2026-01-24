package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "media")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Media {

    public enum MediaType{
        FILE, VIDEO, IMAGE, VOICE
    }

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode // Bắt buộc phải có để so sánh khóa
    public static class MediaId implements Serializable {

        // 1. Map với cột message_id
        private Integer messageId;

        // 2. Map với cột file_name
        @Column(name = "file_name")
        private String fileName;
    }

    @EmbeddedId
    private MediaId id;

    @Column(name = "media_name")
    private String mediaName;

    @Column(name = "mime_type")
    private String mimeType;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", columnDefinition = "VARCHAR(10)")
    private MediaType type;

    @Column(name = "size")
    private Long size;

    @Column(name = "create_at")
    private Instant createdAt;

    // Quan hệ N-1: Nhiều media thuộc về 1 message
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("messageId")
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;
}

