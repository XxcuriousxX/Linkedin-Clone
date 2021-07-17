package com.example.tedi_app.controller;


import com.example.tedi_app.dto.AuthenticationResponse;
import com.example.tedi_app.dto.LoginRequest;
import com.example.tedi_app.dto.RefreshTokenRequest;
import com.example.tedi_app.dto.RegisterRequest;
import com.example.tedi_app.model.User;
import com.example.tedi_app.repo.UserRepository;
import com.example.tedi_app.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {


    private final AuthService authService;
    UserRepository userRepository;


    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest){

        Optional<User> exist = userRepository.findByEmail(registerRequest.getEmail());
        if (exist.isPresent()) {
            return new ResponseEntity<>("username exists",HttpStatus.BAD_REQUEST);
        }

        authService.signup(registerRequest);
        return new ResponseEntity<>("User registered success!",HttpStatus.OK);
    }


    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest){

        return authService.login(loginRequest);
    }


    // prosthiki
    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) throws Exception {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated Successfully", HttpStatus.OK);
    }

    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }
}
