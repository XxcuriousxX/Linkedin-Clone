package com.example.tedi_app.service;


import com.example.tedi_app.dto.*;
import com.example.tedi_app.exceptions.SpringTediException;
import com.example.tedi_app.model.Personalinfo;
import com.example.tedi_app.model.User;
import com.example.tedi_app.model.VerificationToken;
import com.example.tedi_app.repo.PersonalinfoRepository;
import com.example.tedi_app.repo.UserRepository;
import com.example.tedi_app.repo.VerificationTokenRepository;
import lombok.AllArgsConstructor;

import lombok.Getter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.tedi_app.security.JwtProvider;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
@Getter
public class AuthService {


    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final ImageStoreService imageStoreService;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final JwtProvider jwtProvider;

    private final RefreshTokenService refreshTokenService;
    private final PersonalinfoRepository personalInfoRepository;

    @Transactional
    public void signup(RegisterRequest registerRequest) throws Exception {

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setCompany_name(registerRequest.getCompany_name());
        user.setPhone(registerRequest.getPhone());
        user.setFirst_name(registerRequest.getFirst_name());
        user.setLast_name(registerRequest.getLast_name());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        String profile_img = null;


        user.setProfile_picture(profile_img);

        user.setCreated(Instant.now());
        user.setEnabled(false); // false


//        user.personalinfo = new Personalinfo();
        userRepository.save(user);
        User u = userRepository.findByUserId(user.getUserId())
                        .orElseThrow(() -> new SpringTediException("Invalid Token"));
        personalInfoRepository.save(new Personalinfo(u));


        String token = generateVerificationToken(user);
        verifyAccount(token); // verify directly
        System.out.println("\n\nNew user token is : http://localhost:8080/api/auth/accountVerification/" + token);
    }


    @Transactional
    public void changeInfo(ChangeInfoRequest changeInfoRequest) {


        Optional<User>  existing = this.userRepository.findByUsername(changeInfoRequest.getUsername());
        User user1 = existing
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + changeInfoRequest.getUsername()));


        if(changeInfoRequest.getEmail() != null && !changeInfoRequest.getEmail().isEmpty())
            user1.setEmail(changeInfoRequest.getEmail());
        if(changeInfoRequest.getPassword() != null && !changeInfoRequest.getPassword().isEmpty())
            user1.setPassword(passwordEncoder.encode(changeInfoRequest.getPassword()));


        userRepository.save(user1);

    }


    private String generateVerificationToken(User user){
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);

        return token;

    }

    public void verifyAccount(String token) throws Exception {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        fetchUserAndEnable(verificationToken.orElseThrow(() -> new SpringTediException("Invalid Token")));
//        fetchUserAndEnable(verificationToken.get());
    }


    @Transactional
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }

    @Transactional
    public void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringTediException("User not found with name - " + username));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String token = jwtProvider.generateToken(authenticate); // skaei
//        return new AuthenticationResponse(token,loginRequest.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .build();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }
}
