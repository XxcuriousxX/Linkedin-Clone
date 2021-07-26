package com.example.tedi_app.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePersonInfoRequest {
    private String username;
    private String work_experience;
    private String studies;
    private String abilities;

}
