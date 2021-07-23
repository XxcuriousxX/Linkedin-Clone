package com.example.tedi_app.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {

    private String receiver_username;
    private String sender_username;
    private String message;
}
