package com.example.tedi_app.service;


import com.example.tedi_app.dto.ChangePersonInfoRequest;
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

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class PersonalinfoService {

    private final PersonalinfoRepository personalinfoRepository;
    private final UserRepository userRepository;

    public void changePersonalInfo(ChangePersonInfoRequest changePersonInfoRequest) {

        String username = changePersonInfoRequest.getUsername();
        String work_desc = changePersonInfoRequest.getWork_experience();
        String stud_desc = changePersonInfoRequest.getStudies();
        String abilities_desc = changePersonInfoRequest.getAbilities();


        Optional<User> user_opt = userRepository.findByUsername(username);
        User user = user_opt
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username));

        Optional<Personalinfo> usr_pinfo_opt = personalinfoRepository.findByUserId(user.getUserId());
//
//        Personalinfo usr_pinfo = usr_pinfo_opt
//                .orElseThrow(() -> new UsernameNotFoundException("No user " +
//                        "Found with username : " + username));


        if ( personalinfoRepository.findByUserId(user.getUserId()).isPresent() ) {
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
            System.out.println("Personale info: " + work_desc + " !!!");
            System.out.println("Personale info: " + stud_desc + " !!!");
            System.out.println("Personale info: " + abilities_desc + " !!!");
            return;
        }



//        if user has entered at least one time personal info
//        if ( usr_pinfo_opt != null){
//            Personalinfo usr_pinfo = usr_pinfo_opt
//                    .orElseThrow(() -> new UsernameNotFoundException("No user " +
//                            "Found with username : " + username));
//            if ( !usr_pinfo.getAbilities_desc().isBlank())
//                usr_pinfo.setAbilities_desc(abilities_desc);
//            if ( !usr_pinfo.getStud_desc().isBlank())
//                usr_pinfo.setStud_desc(stud_desc);
//            if ( !usr_pinfo.getWork_desc().isBlank())
//                usr_pinfo.setWork_desc(work_desc);
//
//            personalinfoRepository.save(usr_pinfo);
//            return;
//        }
//        else{

            personalinfoRepository.save(new Personalinfo(user.getUserId(),work_desc,stud_desc,abilities_desc) );


//        personalinfoRepository.save(usr_pinfo);



        System.out.println("Personal info: " + work_desc + " !!!");
        System.out.println("Personal info: " + stud_desc + " !!!");
        System.out.println("Personal info: " + abilities_desc + " !!!");
        return;

    }

}
