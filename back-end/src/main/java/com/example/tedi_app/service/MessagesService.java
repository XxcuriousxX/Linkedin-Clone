package com.example.tedi_app.service;

import com.example.tedi_app.dto.MessageResponse;
import com.example.tedi_app.mapper.MessageMapper;
import com.example.tedi_app.model.Message;
import com.example.tedi_app.model.User;
import com.example.tedi_app.repo.MessagesRepository;
import com.example.tedi_app.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class MessagesService {

    private final MessagesRepository messagesRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;

    public void sendMessage(String sender_username, String receiver_username, String msg) {
        Optional<User> receiverUserOptional = userRepository.findByUsername(receiver_username);
        User receiver_user = receiverUserOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + receiver_username));
        Optional<User> senderUserOptional = userRepository.findByUsername(sender_username);
        User sender_user = senderUserOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + sender_username));
        messagesRepository.save(new Message(sender_user.getUserId(), receiver_user.getUserId(), msg));
        System.out.println("Message sent: " + msg + " !!!");
        return;
    }

    public List<MessageResponse> getConversation(String sender_username, String receiver_username) {

        Optional<User> userOptional1 = userRepository.findByUsername(receiver_username);
        User user_rcv = userOptional1
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + receiver_username));

        Optional<User> userOptional2 = userRepository.findByUsername(sender_username);
        User user_sender = userOptional2
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + sender_username));

        Optional<List<Message>> msgListOpt = messagesRepository.getConversation(user_sender.getUserId(),user_rcv.getUserId());
        List<Message> L = msgListOpt.orElseThrow(() -> new UsernameNotFoundException("No user " +
                "Found with username : " + receiver_username));

        for(Message each : L){
            System.out.println(each.toString() + "  : ");
        }

        if (msgListOpt.isEmpty())
            return new ArrayList();

        List<MessageResponse> l = new ArrayList<>();
        for (Message m : L) {
            l.add(messageMapper.mapToDto(m));
        }
        return l;
    }
}
