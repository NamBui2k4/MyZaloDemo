package org.example.repository;

import org.example.entity.Conversation;
import org.example.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Integer> {

    Optional<Participant> findByUser_UserIdAndConversation_ConversationId(
            Integer conversationId,
            Integer userId
    );

    List<Participant> findByConversation_ConversationId(
      Integer conversationId
    );

    @Query("""
        SELECT p1.conversation
        FROM Participant p1
        JOIN Participant p2
             ON p1.conversation = p2.conversation
        WHERE p1.user.userId = :userId1
          AND p2.user.userId = :userId2
    """)
    Optional<Object> findPrivateConversation(
            Integer userId1,
            Integer userId2
    );
}
