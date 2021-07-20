package com.example.tedi_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequest {
    private String sender_username;
    private String receiver_username;
}
