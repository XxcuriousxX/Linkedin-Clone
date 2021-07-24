package com.example.tedi_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

    private Long messageId;
    private String senderUsername;
    private String receiverUsername;
    private String message;
    private Long receiverId;
    private Long senderId;
    private Instant timeCreated;
    private String duration;
}
