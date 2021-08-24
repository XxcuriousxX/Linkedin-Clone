package com.example.tedi_app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonalInfoResponse {

    private String work_desc = "";
    private String stud_desc = "";
    private String abilities_desc = "";

    public PersonalInfoResponse(String wd, String sd, String ad) {
        this.work_desc = wd;
        this.stud_desc = sd;
        this.abilities_desc = ad;
    }
}
