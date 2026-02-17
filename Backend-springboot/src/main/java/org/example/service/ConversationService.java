package org.example.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import org.example.dto.response.ConversationResponse;
import org.example.entity.Conversation;
import org.example.entity.Message;
import org.example.entity.Participant;
import org.example.entity.User;
import org.example.repository.ConversationRepository;
import org.example.repository.ParticipantRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ConversationService {
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;


    @Transactional
    public Conversation createOrGetPrivateConversation(Integer currentUserId, Integer targetUserId) {
        if (currentUserId.equals(targetUserId)) {
            throw new IllegalArgumentException("Cannot chat with yourself");
        }

        // 1️⃣ Check target user tồn tại
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        // 2️⃣ Tìm conversation đã tồn tại
        var existingConversation =
                participantRepository.findPrivateConversation(
                        currentUserId,
                        targetUserId
                );

        if (existingConversation.isPresent()) {
            return (Conversation) existingConversation.get();
        }

        // 3️⃣ Chưa có → tạo mới
        Conversation conversation = new Conversation();
        Conversation savedConversation =
                conversationRepository.save(conversation);

        participantRepository.save(
                Participant.builder()
                        .conversation(savedConversation)
                        .user(currentUser)
                        .build()
        );

        participantRepository.save(
                Participant.builder()
                        .conversation(savedConversation)
                        .user(targetUser)
                        .build()
        );

        return savedConversation;
    }

    @Transactional
    public List<ConversationResponse> getAllConversation(Integer userId) {
        List<Conversation> conversationList = conversationRepository.findUserConversation(userId);

        return conversationList.stream()
                .map(conversation -> {
                    Integer conversationId = conversation.getConversationId();
                    String userName = "Unknown user";
                    String avatarUrl = "";

                    Participant partner = (conversation.getListParticipant() != null)
                            ? conversation.getListParticipant().stream()
                            .filter(p -> !p.getUser().getUserId().equals(userId))
                            .findFirst()
                            .orElse(null)
                            : null;

                    if (partner != null) {
                        userName = partner.getUser().getName();
                        avatarUrl = partner.getUser().getAvatarUrl();
                    }

                    List<Message> messages = conversation.getListMessage();
                    String lastMessage = (messages == null || messages.isEmpty())
                            ? ""
                            : messages.get(messages.size() - 1).getContent();

                    return new ConversationResponse(
                            conversationId,
                            userName,
                            lastMessage,
                            avatarUrl
                    );
                })
                .collect(Collectors.toList());
    }


}
