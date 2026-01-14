package org.example.repository;

import org.example.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ConversationRepository extends JpaRepository<Conversation, Integer>{


}
