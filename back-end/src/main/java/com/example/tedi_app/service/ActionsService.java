package com.example.tedi_app.service;

import com.example.tedi_app.dto.ActionResponse;
import com.example.tedi_app.dto.ChangeInfoRequest;
import com.example.tedi_app.dto.FriendResponse;
import com.example.tedi_app.dto.SearchResponse;
import com.example.tedi_app.exceptions.SpringTediException;
import com.example.tedi_app.model.Action;
import com.example.tedi_app.model.Friends;
import com.example.tedi_app.model.ListOfFriends;
import com.example.tedi_app.model.User;
import com.example.tedi_app.repo.ActionsRepository;
import com.example.tedi_app.repo.FriendsRepository;
import com.example.tedi_app.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.*;
import java.util.Optional;

import static java.util.Collections.singletonList;

@Service
@AllArgsConstructor
public class ActionsService {
    private final UserRepository userRepository;
    private final FriendsRepository friendsRepository;
    private final ActionsRepository actionsRepository;

    private static ActionResponse mapToDto(Action a) {
        return new ActionResponse(a.getFromUser().getUsername(),
                                    a.getActionType(),a.getPost().getPostId(), a.getTimeCreated());
    }


    public List<ActionResponse> getAllNotificationsByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No user " +
                "Found with username: "+ username));

        Optional<List<Action>> listOptional = actionsRepository.findAllByToUserOrderByTimeCreatedAsc(user);
        if (listOptional.isEmpty())
            return new ArrayList<>();
        List<Action> actList = listOptional.orElseThrow(() -> new UsernameNotFoundException("No acts " +
                "found, directed to: "+ username));
        List<ActionResponse> actRespList = new ArrayList<>();
        for (Action a : actList) {
            if (a.getFromUser().equals(username)) // skip actions done by myself
                continue;
            System.out.println("Action " + a.toString());
            actRespList.add(mapToDto(a));
        }
        return actRespList;

    }
}