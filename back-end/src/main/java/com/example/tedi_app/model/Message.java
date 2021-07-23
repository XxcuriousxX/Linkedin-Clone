package com.example.tedi_app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.time.Instant;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long messageId;


    private String message;
    private Long receiverId;
    private Long senderId;

    private Instant timeCreated = Instant.now();

    public Message(Long sender_id, Long receiver_id, String message) {
        this.senderId = sender_id;
        this.receiverId = receiver_id;
        this.message = message;
    }


}
