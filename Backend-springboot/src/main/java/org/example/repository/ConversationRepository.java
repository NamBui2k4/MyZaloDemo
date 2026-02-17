package org.example.repository;

import org.example.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Integer>{
    @Query("""
            SELECT DISTINCT conversation
            FROM Conversation conversation
            JOIN conversation.listParticipant participant
            WHERE participant.user.userId =:userId
            """)
    List<Conversation> findUserConversation(Integer userId);
}
