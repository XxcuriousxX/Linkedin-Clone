package com.example.tedi_app.service;


import com.example.tedi_app.dto.ChangeButtonStateRequest;
import com.example.tedi_app.dto.ChangePersonInfoRequest;
import com.example.tedi_app.model.Personalinfo;
import com.example.tedi_app.model.PublicButton;
import com.example.tedi_app.model.User;
import com.example.tedi_app.repo.PersonalinfoRepository;
import com.example.tedi_app.repo.PublicButtonsRepository;
import com.example.tedi_app.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class PublicButtonService {


    private final PublicButtonsRepository publicButtonsRepository;
    private final UserRepository userRepository;


    public void changeButtonState(ChangeButtonStateRequest changeButtonStateRequest) {


        Optional<User> user_opt = userRepository.findByUsername(changeButtonStateRequest.getUsername());
        User user = user_opt
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + changeButtonStateRequest.getUsername()));

        Optional<PublicButton> usr_btn_opt = publicButtonsRepository.findByUserId(user.getUserId());


        if ( publicButtonsRepository.findByUserId(user.getUserId()).isPresent() ) {
            PublicButton usr_btn = usr_btn_opt
                    .orElseThrow(() -> new UsernameNotFoundException("No user " +
                            "Found with username : "));

            usr_btn.setAbilities(changeButtonStateRequest.isAbilities());
            usr_btn.setCompany(changeButtonStateRequest.isCompany());
            usr_btn.setPhone(changeButtonStateRequest.isPhone());
            usr_btn.setStudies(changeButtonStateRequest.isStudies());
            usr_btn.setWork_exp(changeButtonStateRequest.isWork_exp());

            publicButtonsRepository.save(usr_btn);
            return;

        }
        else {
            publicButtonsRepository.save(new PublicButton(user.getUserId()));
        }

    }



    public PublicButton get_button_state(String username){
        Optional<User> user_opt = userRepository.findByUsername(username);
        User user = user_opt
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username));

        Optional<PublicButton> usr_btn_opt = publicButtonsRepository.findByUserId(user.getUserId());
        PublicButton usr_btn = usr_btn_opt
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username));


        return usr_btn;

    }

}
