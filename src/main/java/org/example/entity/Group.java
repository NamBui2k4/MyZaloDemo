package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.List;
import lombok.experimental.SuperBuilder;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@SuperBuilder
@Entity
@Table(name = "user_group")
@DiscriminatorValue("GROUP")
@PrimaryKeyJoinColumn(name = "group_id")
public class Group extends Conversation {

    @Column(name = "create_at", nullable = false)
    private Instant timecreateAt;

    @Column(name = "num_member", nullable = false)
    private Integer numberMember;

    @Column(name = "group_name", nullable = false)
    private String groupName;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<Member> members;
}
