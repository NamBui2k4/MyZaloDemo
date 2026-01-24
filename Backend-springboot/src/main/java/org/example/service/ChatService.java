package org.example.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.entity.*;
import org.example.repository.MessageRepository;
import org.example.repository.MessageStatusRepository;
import org.example.repository.UserRepository;
import org.example.repository.ConversationRepository;
import org.example.exception.UserNotFoundException;
import org.example.exception.ConversationNotFoundException;
import org.example.repository.ParticipantRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;


@Service
@AllArgsConstructor
public class ChatService {
    private final MessageRepository messageRepo;
    private final MessageStatusRepository messageStatusRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;

    public Message sendMessage(Integer conversationId, Integer senderId, String content){

        Conversation conversation = conversationRepository
                .findById(conversationId)
                .orElseThrow(() -> new ConversationNotFoundException("Conversation not found"));

        User sender = userRepository.findById(senderId).orElseThrow(
                () -> new UserNotFoundException("Cannot find user")
        );

        participantRepository.findByUser_UserIdAndConversation_ConversationId(
                senderId, conversationId
        ).orElseThrow(() ->
                new UserNotFoundException("User not in conversation")
        );

        Message message = Message.builder()
                .conversation(conversation)
                .sender(sender)
                .content(content)
                .build();
        Message saved = messageRepo.save(message);
        createInitialStatuses(message, sender);
        return saved;
    }

    private void createInitialStatuses(Message message, User sender) {

        List<Participant> participants =
                participantRepository.findByConversation_ConversationId(
                        message.getConversation().getConversationId()
                );

        for (Participant p : participants) {
            if (!p.getUser().getUserId().equals(sender.getUserId())) {

                MessageStatus status = MessageStatus.builder()
                        .statusId(new MessageStatus.MessageStatusId(
                                message.getMessageId(),
                                p.getUser().getUserId()
                        ))
                        .message(message)
                        .receiver(p.getUser())
                        .status(MessageStatus.MessageReceiptStatus.SENT)
                        .sentTime(Instant.now())
                        .receiveTime(Instant.now())
                        .build();

                messageStatusRepository.save(status);
            }
        }
    }

    public List<Message> getMessages(Integer conversationId) {
        return messageRepo
                .findByConversation_ConversationIdOrderByMessageIdAsc(
                        conversationId
                );
    }

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
    public MessageStatus markAsDelivered(Integer messageId, Integer receiverId ) {
        // Tìm status tương ứng (bạn có thể cần tìm theo cả userId người nhận để chính xác)
        MessageStatus status = messageStatusRepository.findByMessageIdAndReceiverId(messageId, receiverId) // Giả sử lấy được current user
                .orElseThrow(() -> new RuntimeException("Status not found"));

        if (status.getStatus() == MessageStatus.MessageReceiptStatus.SENT) {
            status.setStatus(MessageStatus.MessageReceiptStatus.DELIVERED);
            status.setReceiveTime(Instant.now()); // LÚC NÀY MỚI SET TIME
        }
        return messageStatusRepository.save(status);
    }

    @Transactional
    public MessageStatus markAsSeen(Integer messageId, Integer receiverId) {
        MessageStatus status = messageStatusRepository.findByMessageIdAndReceiverId(messageId, receiverId)
                .orElseThrow(() -> new RuntimeException("Status not found"));

        // Chỉ update nếu chưa seen
        if (status.getStatus() != MessageStatus.MessageReceiptStatus.SEEN) {
            status.setStatus(MessageStatus.MessageReceiptStatus.SEEN);
            status.setSeenTime(Instant.now()); // LÚC NÀY MỚI SET TIME

            // Nếu chưa có receive time (trường hợp mở xem luôn mà bỏ qua bước delivered)
            if (status.getReceiveTime() == null) {
                status.setReceiveTime(Instant.now());
            }
        }
        return messageStatusRepository.save(status);
    }

    @Transactional
    public Group createGroup( Integer creatorId, String groupName, List<Integer> memberIds){
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new UserNotFoundException("Can not find user for group creation"));



        return null;
    }

}
