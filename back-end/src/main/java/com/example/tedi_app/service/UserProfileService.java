package com.example.tedi_app.service;


import com.example.tedi_app.dto.UserProfileResponse;
import com.example.tedi_app.model.Personalinfo;
import com.example.tedi_app.model.PublicButton;
import com.example.tedi_app.model.User;
import com.example.tedi_app.repo.PersonalinfoRepository;
import com.example.tedi_app.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserProfileService {


    private final UserDetailsServiceImpl userDetailsService;
    private final PersonalinfoService personalinfoService;



    public UserProfileResponse getUserProfileInfo(String username){

        User usr = userDetailsService.get_user_info(username);

        Personalinfo user_personal_info = personalinfoService.getPersonalInfo(username);

        return new UserProfileResponse(usr,user_personal_info);

    }
}
