package com.example.tedi_app.controller;

import com.example.tedi_app.dto.FriendRequest;
import com.example.tedi_app.dto.SearchResponse;
import com.example.tedi_app.model.User;
import com.example.tedi_app.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserDetailsServiceImpl userService;


    @GetMapping("/{username}")
    public ResponseEntity <List<User>> getAllConnectedUsers(@PathVariable String username){
        return status(HttpStatus.OK).body(userService.get_all_connected_users(username));
    }

//    make request for friend connection
    @PostMapping("/connect_request/")
    public ResponseEntity<Void> makeConnectionRequest(@RequestBody FriendRequest friendRequest) {
        this.userService.makeConnectionRequest(friendRequest.getSender_username(), friendRequest.getReceiver_username());
        return new ResponseEntity<>(HttpStatus.OK);
    }


    // Remember: sender of the request, has sent friendRequest = {sender_username, receiver_username}
    // Receiver user will also send friendRequest = { sender_username, receiver_username}
    @PostMapping("/accept_connection_request/")
    public ResponseEntity<Void> acceptRequest(@RequestBody FriendRequest friendRequest) {
        this.userService.acceptRequest(friendRequest.getSender_username(), friendRequest.getReceiver_username());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Remember: sender of the request, has sent friendRequest = {sender_username, receiver_username}
    // Receiver user will also send friendRequest = { sender_username, receiver_username}
    @PostMapping("/reject_connection_request/")
    public ResponseEntity<Void> rejectRequest(@RequestBody FriendRequest friendRequest) {
        this.userService.rejectRequest(friendRequest.getSender_username(), friendRequest.getReceiver_username());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/search/{query}")
    public ResponseEntity<List<User>> getSearchResults(@PathVariable String query){

        return status(HttpStatus.OK).body(this.userService.getUsersByQuery(query));
    }
}


