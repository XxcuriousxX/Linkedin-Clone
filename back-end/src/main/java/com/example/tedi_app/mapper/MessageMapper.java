package com.example.tedi_app.mapper;
import com.example.tedi_app.dto.MessageRequest;
import com.example.tedi_app.dto.MessageResponse;
import com.example.tedi_app.repo.UserRepository;
import com.example.tedi_app.service.MessagesService;
import com.example.tedi_app.service.UserDetailsServiceImpl;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.example.tedi_app.dto.PostRequest;
import com.example.tedi_app.dto.PostResponse;
import com.example.tedi_app.model.*;
import com.example.tedi_app.repo.CommentRepository;
import com.example.tedi_app.repo.VoteRepository;
import com.example.tedi_app.service.AuthService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;


@Mapper(componentModel = "spring")
public abstract class MessageMapper {

    @Autowired
    private AuthService authService;

    

    @Mapping(target = "messageId", source = "messageId")
    @Mapping(target = "message", source = "message")
    @Mapping(target = "senderId", source = "senderId")
    @Mapping(target = "receiverId", source = "receiverId")
    @Mapping(target = "timeCreated", source = "timeCreated")
    @Mapping(target = "stringTimeCreated", expression= "java(getStringTimeCreated(msg))")
    @Mapping(target = "duration", expression = "java(getDuration(msg))")
    public abstract MessageResponse mapToDto(Message msg);

    String getDuration(Message msg) {
        return TimeAgo.using(msg.getTimeCreated().toEpochMilli());
    }

    String getStringTimeCreated(Message msg) {
        Date myDate = Date.from(msg.getTimeCreated());
        SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyyy HH:mm");
        String formattedDate = formatter.format(myDate);
        return formattedDate;
    }

}
