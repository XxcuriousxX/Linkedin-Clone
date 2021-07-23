package com.example.tedi_app.controller;

import com.example.tedi_app.dto.MessageRequest;
import com.example.tedi_app.model.Message;
import com.example.tedi_app.service.MessagesService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/messages")
@AllArgsConstructor
public class MessagesController {
    private final MessagesService messagesService;


    @PostMapping("/send_message/")
    public ResponseEntity<String> sendMessage(@RequestBody MessageRequest msgRequest) {
        System.out.println("Entered message sent");
        messagesService.sendMessage(msgRequest.getSender_username()
                , msgRequest.getReceiver_username(), msgRequest.getMessage());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/get_conversation/")
    public ResponseEntity<List<Message>> getConversation(@RequestBody MessageRequest msgRequest) {
        return status(HttpStatus.OK).body(messagesService.getConversation(msgRequest.getSender_username(), msgRequest.getReceiver_username()));

    }
}
