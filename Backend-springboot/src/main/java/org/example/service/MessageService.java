package org.example.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.dto.socket.MessageStatusDTO;
import org.example.entity.*;
import org.example.exception.EntityNotFoundException;
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
public class MessageService {
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
    public MessageStatusDTO markAsDelivered(Integer messageId, Integer receiverId ) {
        // Tìm status tương ứng (bạn có thể cần tìm theo cả userId người nhận để chính xác)
        MessageStatus ms = messageStatusRepository.findByMessageIdAndReceiverId(messageId, receiverId) // Giả sử lấy được current user
                .orElseThrow(() -> new EntityNotFoundException("Status not found"));


        if (ms.getStatus() != MessageStatus.MessageReceiptStatus.SENT) {
            throw new IllegalStateException(
                    "Cannot mark as DELIVERED from state " + ms.getStatus()
            );
        }

        ms.setStatus(MessageStatus.MessageReceiptStatus.DELIVERED);
        ms.setReceiveTime(Instant.now());

        return new MessageStatusDTO(
                ms.getMessage().getMessageId(),
                ms.getMessage().getSender().getUserId(),
                ms.getStatus(),
                ms.getReceiveTime()
        );
    }


    @Transactional
    public MessageStatusDTO markAsSeen(Integer messageId, Integer receiverId) {

        MessageStatus ms = messageStatusRepository.findByMessageIdAndReceiverId(messageId, receiverId)
                .orElseThrow(() -> new RuntimeException("Status not found"));

        switch (ms.getStatus()){
            case DELIVERED -> {
                ms.setStatus(MessageStatus.MessageReceiptStatus.SEEN);
                ms.setSeenTime(Instant.now()); // LÚC NÀY MỚI SET TIME
                // Nếu chưa có receive time (trường hợp mở xem luôn mà bỏ qua bước delivered)
                if (ms.getReceiveTime() == null) {
                    ms.setReceiveTime(Instant.now());
                }
            }
            case SEEN -> {
                // nếu "đã xem" thì không làm gì
            }
            default -> {
                throw new IllegalStateException(
                        "Cannot mark SEEN from state" + ms.getStatus()
                );
            }
        }


        return new MessageStatusDTO(
                ms.getMessage().getMessageId(),
                ms.getMessage().getSender().getUserId(),
                ms.getStatus(),
                ms.getReceiveTime()
        );
    }

    @Transactional
    public Group createGroup( Integer creatorId, String groupName, List<Integer> memberIds){
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new UserNotFoundException("Can not find user for group creation"));



        return null;
    }

}
