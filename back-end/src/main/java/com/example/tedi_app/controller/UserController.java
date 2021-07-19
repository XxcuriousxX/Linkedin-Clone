package com.example.tedi_app.controller;

import com.example.tedi_app.dto.SearchResponse;
import com.example.tedi_app.model.User;
import com.example.tedi_app.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}


