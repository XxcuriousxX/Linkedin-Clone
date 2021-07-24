package com.example.tedi_app.model;

import lombok.*;

import javax.persistence.*;

import java.time.Instant;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
@Setter
public class Message {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long messageId;


    private String message;
    private Long receiverId;
    private Long senderId;


//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "receiverId", referencedColumnName = "userId")
//    private User receiverUser;
//
//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "senderId", referencedColumnName = "userId")
//    private User senderUser;




    private Instant timeCreated = Instant.now();

    public Message(Long sender_id, Long receiver_id, String message) {
        this.senderId = sender_id;
        this.receiverId = receiver_id;
        this.message = message;
    }


}
