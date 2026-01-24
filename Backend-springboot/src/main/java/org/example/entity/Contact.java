package org.example.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contact")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    private Integer contactId;

    @Column(name = "contact_name")
    private String contactName;

    private String phone;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
