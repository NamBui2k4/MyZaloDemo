package org.example.repository;

import org.example.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findByConversation_ConversationIdOrderByMessageIdAsc(
            Integer conversationId
    );

}
