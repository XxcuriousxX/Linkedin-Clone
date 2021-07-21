package com.example.tedi_app.service;

import com.example.tedi_app.dto.FriendResponse;
import com.example.tedi_app.dto.SearchResponse;
import com.example.tedi_app.exceptions.SpringTediException;
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

    public void makeConnectionRequest(String sender_username, String receiver_username) {
        Optional<User> receiverUserOptional = userRepository.findByUsername(receiver_username);
        User receiver_user = receiverUserOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + receiver_username));
        Optional<User> senderUserOptional = userRepository.findByUsername(sender_username);
        User sender_user = senderUserOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + sender_username));

        // create a friends row with accepted value: 'false'
        friendsRepository.save(new Friends(sender_user.getUserId(), receiver_user.getUserId()));

    }

    public void acceptRequest(String sender_username,String receiver_username ) {
        Optional<User> receiverUserOptional = userRepository.findByUsername(receiver_username);
        User receiver_user = receiverUserOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + receiver_username));
        Optional<User> senderUserOptional = userRepository.findByUsername(sender_username);
        User sender_user = senderUserOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + sender_username));

        Optional<Friends> requestOptional = friendsRepository
                        .getFriendsByUserId1AndUserId2(sender_user.getUserId(), receiver_user.getUserId());
        Friends request = requestOptional
                        .orElseThrow(() -> new UsernameNotFoundException("No such connection request was found"));
        Optional<Boolean> areConnectedOptional = friendsRepository
                        .CheckForRequest(sender_user.getUserId(), receiver_user.getUserId());
        if (areConnectedOptional.isPresent()) { // if request exists
            Boolean areConnected = areConnectedOptional.orElseThrow(() -> new UsernameNotFoundException("No connection " +
                    "from : " + sender_username));
            if (areConnected)
                throw new SpringTediException("Already connected!");
            // replace the request with the same id, but with accpted = true
            friendsRepository.save(new Friends(request.getId(), request.getUser_id1(), request.getUser_id2(), true));
            return;
        }
        throw new UsernameNotFoundException("Request from " + sender_username + " to " + receiver_username + " was not found");

    }


    //    reject request
    // direction matters
    public void rejectRequest(String sender_username,String receiver_username ) {
        Optional<User> receiverUserOptional = userRepository.findByUsername(receiver_username);
        User receiver_user = receiverUserOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + receiver_username));
        Optional<User> senderUserOptional = userRepository.findByUsername(sender_username);
        User sender_user = senderUserOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + sender_username));

        Optional<Friends> requestOptional = friendsRepository
                .getFriendsByUserId1AndUserId2(sender_user.getUserId(), receiver_user.getUserId());
        Friends request = requestOptional
                .orElseThrow(() -> new UsernameNotFoundException("No connection request was found"));


        Optional<Boolean> areConnectedOptional = friendsRepository // maybe call connectionRequestExists()
                .CheckForRequest(sender_user.getUserId(), receiver_user.getUserId());
        if (areConnectedOptional.isPresent()) { // if request exists
            Boolean areConnected = areConnectedOptional.orElseThrow(() -> new UsernameNotFoundException("No connection " +
                    "from : " + sender_username));
            if (areConnected)
                throw new SpringTediException("Already connected!");


            friendsRepository.deleteById(request.getId());
            return;


        }
        throw new UsernameNotFoundException("Request from " + sender_username + " to " + receiver_username + " was not found");

    }


    public List<User> getUsersByQuery(String query) {
        Optional<List<User>> fetched_users_optional = userRepository.getUsersByQuery(query);
        if (!fetched_users_optional.isPresent())
            return new ArrayList<User>(); // empty set
        List<User> fetched_users = fetched_users_optional.orElseThrow(() -> new UsernameNotFoundException("No such connection request was found"));
        System.out.println(fetched_users);
        return fetched_users;
    }

    public FriendResponse areConnected(String username1, String username2) {
        Optional<User> receiverUserOptional = userRepository.findByUsername(username1);
        User user1 = receiverUserOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username1));
        Optional<User> senderUserOptional = userRepository.findByUsername(username2);
        User user2 = senderUserOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username2));

        Optional<Boolean> areConnectedOptional = friendsRepository
                .areConnected(user1.getUserId(), user2.getUserId());
        if (areConnectedOptional.isPresent()) { // if exists
            Boolean areConnected = areConnectedOptional.orElseThrow(() -> new UsernameNotFoundException("No connection tuple " +
                    "between : " + username1 + " and " + username2));
            if (areConnected)
                return new FriendResponse(true);
            else
                return new FriendResponse(false);
        } else
            return new FriendResponse(false);
    }


    // true if sender waits for the receiver to accept it
    // false if there has been no connection request
    // Just checks if there is a tuple {sender, receiver}  (direction matters)
    public FriendResponse isPendingConnectionRequest(String username1, String username2) {
        Optional<User> receiverUserOptional = userRepository.findByUsername(username1);
        User user1 = receiverUserOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username1));
        Optional<User> senderUserOptional = userRepository.findByUsername(username2);
        User user2 = senderUserOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username2));
        Optional<Friends> requestOptional = friendsRepository
                .getFriendsByUserId1AndUserId2(user1.getUserId(), user2.getUserId());


        if (requestOptional.isPresent())
            return new FriendResponse(true);
        else
            return new FriendResponse(false);
    }


    // removes connection or connection request between username1 and username2. Direction does not matter
    public void removeConnection(String username1, String username2) {
        Optional<User> receiverUserOptional = userRepository.findByUsername(username1);
        User user1 = receiverUserOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username1));
        Optional<User> senderUserOptional = userRepository.findByUsername(username2);
        User user2 = senderUserOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username2));
        Optional<Friends> requestOptional = friendsRepository
                .getExistingConnection(user1.getUserId(), user2.getUserId());
        if (!requestOptional.isPresent())
            throw new SpringTediException("There is no connection or connection request to be removed");

        Friends request = requestOptional.orElseThrow(() -> new UsernameNotFoundException("No connection tuple " +
                "between : " + username1 + " and " + username2));
        friendsRepository.deleteById(request.getId()); // remove the connection from the DB
    }
}