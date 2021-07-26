package com.example.tedi_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeButtonStateRequest {

    private String username;
    private boolean work_exp;
    private boolean studies;
    private boolean abilities;
    private boolean company;
    private boolean phone;



}