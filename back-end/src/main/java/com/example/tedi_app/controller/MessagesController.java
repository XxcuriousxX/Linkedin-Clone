package com.example.tedi_app.controller;

import com.example.tedi_app.dto.MessageRequest;
import com.example.tedi_app.dto.MessageResponse;
import com.example.tedi_app.model.Message;
import com.example.tedi_app.model.User;
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
        messagesService.sendMessage(msgRequest.getSender_username()
                , msgRequest.getReceiver_username(), msgRequest.getMessage());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/get_conversation/")
    public ResponseEntity<List<MessageResponse>> getConversation(@RequestBody MessageRequest msgRequest) {
        return status(HttpStatus.OK).body(messagesService.getConversation(msgRequest.getSender_username(), msgRequest.getReceiver_username()));
    }

    @PostMapping("/more_messages/")
    public ResponseEntity<List<MessageResponse>> more_messages(@RequestBody MessageResponse msgResponse) {
        return status(HttpStatus.OK).body(messagesService.loadMoreMessages(msgResponse));
    }

    @GetMapping("/get_conversation_names/")
    public ResponseEntity<List<User>> get_conversation_names() {
        return status(HttpStatus.OK).body(messagesService.get_conversation_names());
    }



}
