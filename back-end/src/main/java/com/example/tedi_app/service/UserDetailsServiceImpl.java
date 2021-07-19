package com.example.tedi_app.service;

import com.example.tedi_app.dto.SearchResponse;
import com.example.tedi_app.model.Friends;
import com.example.tedi_app.model.ListOfFriends;
import com.example.tedi_app.model.User;
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
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final FriendsRepository friendsRepository;
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username));

        return new org.springframework.security
                .core.userdetails.User(user.getUsername(), user.getPassword(),
                user.isEnabled(), true, true,
                true, getAuthorities("USER"));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return singletonList(new SimpleGrantedAuthority(role));
    }

    // returns a list of Users who are friendss of username
    public List<User> get_all_connected_users(String username) {
//        return userRepository.getAllUsers(
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username));

        System.out.println("\nUser = " + user.getUsername());

        List<Friends> F = friendsRepository.getAllConnectedUsers(user.getUserId());

        System.out.println("\nUser11111111 = " + user.getUsername());

        ArrayList<User> friends_list = new ArrayList<>();
        Long id = user.getUserId();
        System.out.println("\nID = " + id);

        for (Friends friend : F) {
            System.out.println("Friend = " + friend.getUser_id1() + "  " + friend.getUser_id2());
            if (friend.getUser_id1() != id) {
                User u1 = userRepository.findByUserId(friend.getUser_id1()).orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username));
                friends_list.add(u1);
            }
            if (friend.getUser_id2() != id) {
                User u2 = userRepository.findByUserId(friend.getUser_id2()).orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username));
                friends_list.add(u2);
            }
        }
        return friends_list;

    }

}