package com.example.tedi_app.dto;


import com.example.tedi_app.model.Personalinfo;
import com.example.tedi_app.model.User;
import lombok.*;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserProfileResponse {

    private String username;
    private String email;
    private String phone;
    private String first_name;
    private String last_name;
    private String company_name;
    private String work_experience;
    private String studies;
    private String abilities;

    public UserProfileResponse(User usr, Personalinfo user_personal_info) {
        this.username = usr.getUsername();
        this.email = usr.getEmail();
        this.phone = usr.getPhone();
        this.first_name = usr.getFirst_name();
        this.last_name = usr.getLast_name();
        this.company_name = usr.getCompany_name();
        this.work_experience = user_personal_info.getWork_desc();
        this.studies = user_personal_info.getStud_desc();
        this.abilities = user_personal_info.getAbilities_desc();
    }
}
