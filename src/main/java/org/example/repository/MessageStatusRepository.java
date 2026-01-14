package org.example.repository;

import org.example.entity.MessageStatus;
import org.example.entity.MessageStatus.MessageStatusId;
import org.example.entity.MessageStatus.MessageReceiptStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MessageStatusRepository
        extends JpaRepository<MessageStatus, MessageStatusId> {

    List<MessageStatus> findByMessage_MessageId(Integer messageId);

    Optional<MessageStatus> findByMessage_MessageIdAndReceiver_UserId(
            Integer messageId,
            Integer userId
    );

    long countByMessage_MessageIdAndStatus(
            Integer messageId,
            MessageReceiptStatus status
    );

    @Query("SELECT s FROM MessageStatus s WHERE s.statusId.messageId = :messageId AND s.statusId.userId = :userId")
    Optional<MessageStatus> findByMessageIdAndReceiverId(Integer messageId, Integer userId);
}
