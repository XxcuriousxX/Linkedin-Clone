package com.example.tedi_app.controller;

import com.example.tedi_app.dto.*;
import com.example.tedi_app.model.Action;

import com.example.tedi_app.model.User;
import com.example.tedi_app.repo.UserRepository;
import com.example.tedi_app.service.ActionsService;
import com.example.tedi_app.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserDetailsServiceImpl userService;
    private UserRepository userRepository;
    private final ActionsService actionsService;


    @GetMapping("/{username}")
    public ResponseEntity <List<User>> getAllConnectedUsers(@PathVariable String username){
        return status(HttpStatus.OK).body(userService.get_all_connected_users(username));
    }

    @GetMapping("/get_user_by_id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return status(HttpStatus.OK).body(userService.getUserById(id));
    }
//    make request for friend connection
    @PostMapping("/connect_request")
    public ResponseEntity<Void> makeConnectionRequest(@RequestBody FriendRequest friendRequest) {
        this.userService.makeConnectionRequest(friendRequest.getSender_username(), friendRequest.getReceiver_username());
        System.out.println("==makeConnectioRequest just executed\n");
        return new ResponseEntity<>(HttpStatus.OK);
    }


    // Remember: sender of the request, has sent friendRequest = {sender_username, receiver_username}
    // Receiver user will also have to send friendRequest = { sender_username, receiver_username}
    @PostMapping("/accept_connection_request")
    public ResponseEntity<Void> acceptRequest(@RequestBody FriendRequest friendRequest) {
        this.userService.acceptRequest(friendRequest.getSender_username(), friendRequest.getReceiver_username());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Remember: sender of the request, has sent friendRequest = {sender_username, receiver_username}
    // Receiver user will also send friendRequest = { sender_username, receiver_username}
    @PostMapping("/reject_connection_request")
    public ResponseEntity<Void> rejectRequest(@RequestBody FriendRequest friendRequest) {
        this.userService.rejectRequest(friendRequest.getSender_username(), friendRequest.getReceiver_username());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/search/{query}")
    public ResponseEntity<List<User>> getSearchResults(@PathVariable String query){

        return status(HttpStatus.OK).body(this.userService.getUsersByQuery(query));
    }

    // checks if users are connected. Direction does not matter {sender, receiver} is same with {receiver, sender}
    @PostMapping("/are_connected")
    public ResponseEntity<FriendResponse> areConnected(@RequestBody FriendRequest friendRequest) {
        return  status(HttpStatus.OK).body(this.userService.areConnected(friendRequest.getSender_username()
                                                    , friendRequest.getReceiver_username()));
    }

    // checks if a request exists (direction matters)
    @PostMapping("/pending_request")
    public ResponseEntity<FriendResponse> isPendingConnectionRequest(@RequestBody FriendRequest friendRequest) {
        return  status(HttpStatus.OK).body(this.userService.isPendingConnectionRequest(friendRequest.getSender_username()
                , friendRequest.getReceiver_username()));
    }

    // removes connection or connection request. Direction does not matter
    // This function removes the tuple {sender, receiver} or {receiver, sender}
    @PostMapping("/remove_connection")
    public ResponseEntity<Void> removeConnection(@RequestBody FriendRequest friendRequest) {
        this.userService.removeConnection(friendRequest.getSender_username()
                , friendRequest.getReceiver_username());
        return  new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/get_all_pending_requests_sent_to_user/{username}")
    public ResponseEntity<List<User>> getAllPendingRequestsSentToUser(@PathVariable String username) {
        return status(HttpStatus.OK).body(this.userService.getAllPendingRequestsSentToUser(username));
    }


    /// User info

    @PostMapping("/changeinfo")
    public ResponseEntity<String> ChangeInfo(@RequestBody ChangeInfoRequest changeInfoRequest){

        if(this.userRepository.findByEmail(changeInfoRequest.getEmail()).isPresent()){
            return new ResponseEntity<>("email exists", HttpStatus.BAD_REQUEST);
        }

        userService.changeInfo(changeInfoRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }


    @GetMapping("/userInfo/{username}")
    public ResponseEntity<User> getUserInfo(@PathVariable String username){
        return status(HttpStatus.OK).body(userService.get_user_info(username));
    }

    // Actions - notifications
    @GetMapping("/get_notifications/{username}")
    public ResponseEntity<List<ActionResponse>> getAllUserNotifications(@PathVariable String username) {
        return status(HttpStatus.OK).body(actionsService.getAllNotificationsByUsername(username));
    }
}


