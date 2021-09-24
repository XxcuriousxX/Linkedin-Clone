package com.example.tedi_app.service;


import com.example.tedi_app.dto.ChangePersonInfoRequest;
import com.example.tedi_app.dto.ChangeProfileImageRequest;
import com.example.tedi_app.model.Message;
import com.example.tedi_app.model.Personalinfo;
import com.example.tedi_app.model.User;
import com.example.tedi_app.repo.MessagesRepository;
import com.example.tedi_app.repo.PersonalinfoRepository;
import com.example.tedi_app.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class PersonalinfoService {

    private final PersonalinfoRepository personalinfoRepository;
    private final UserRepository userRepository;
    private final ImageStoreService imageStoreService;


    public void changePersonalInfo(ChangePersonInfoRequest changePersonInfoRequest) {

        String username = changePersonInfoRequest.getUsername();
        String work_desc = changePersonInfoRequest.getWork_experience();
        String stud_desc = changePersonInfoRequest.getStudies();
        String abilities_desc = changePersonInfoRequest.getAbilities();


        Optional<User> user_opt = userRepository.findByUsername(username);
        User user = user_opt
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username));

        Optional<Personalinfo> usr_pinfo_opt = personalinfoRepository.findByUserUserId(user.getUserId());



        if ( personalinfoRepository.findByUserUserId(user.getUserId()).isPresent() ) {
            Personalinfo usr_pinfo = usr_pinfo_opt
                    .orElseThrow(() -> new UsernameNotFoundException("No user " +
                            "Found with username : " + username));
            if ( abilities_desc != null)
                usr_pinfo.setAbilities_desc(abilities_desc);
            if ( stud_desc!= null)
                usr_pinfo.setStud_desc(stud_desc);
            if ( work_desc!= null)
                usr_pinfo.setWork_desc(work_desc);

            personalinfoRepository.save(usr_pinfo);
//            System.out.println("Personale info: " + work_desc + " !!!");
//            System.out.println("Personale info: " + stud_desc + " !!!");
//            System.out.println("Personale info: " + abilities_desc + " !!!");
            return;
        }

        //
        personalinfoRepository.save(new Personalinfo(user, work_desc,stud_desc,abilities_desc) );




//        System.out.println("Personal info: " + work_desc + " !!!");
//        System.out.println("Personal info: " + stud_desc + " !!!");
//        System.out.println("Personal info: " + abilities_desc + " !!!");
        return;

    }


    public Personalinfo getPersonalInfo(String username){

        Optional<User> user_opt = userRepository.findByUsername(username);
        User user = user_opt
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username));

        Optional<Personalinfo> userPersonal_opt = personalinfoRepository.findByUserUserId(user.getUserId());
        Personalinfo  userPersonal = userPersonal_opt
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username));

        return userPersonal;

    }


    public void changeProfileImage(ChangeProfileImageRequest changeProfileImageRequest) {
        Optional<User> user_opt = userRepository.findByUsername(changeProfileImageRequest.getUsername());
        User user = user_opt
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + changeProfileImageRequest.getUsername()));

        String img;
        if (changeProfileImageRequest.getMultipartFile() != null) {
            // Store profile image
            img = imageStoreService.save_img(changeProfileImageRequest.getMultipartFile());
        } else {
            img = null;
        }

        user.setProfile_picture(img);

    }


}
