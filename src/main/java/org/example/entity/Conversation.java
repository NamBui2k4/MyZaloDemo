package org.example.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.repository.query.parser.Part;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@SuperBuilder
@Entity
@Table(name = "conversation")
@Inheritance(strategy = InheritanceType.JOINED)
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conversation_id")
    private Integer conversationId;

    @Column(name = "create_at", nullable = false)
    @CreationTimestamp
    private Instant createAt;


    /* === RELATIONS === */

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "conversation")
    private List<Participant> listParticipant;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "conversation")
    private List<Message> listMessage;


}
