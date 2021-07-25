package com.example.tedi_app.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeInfoRequest {
    private String email;
    private String password;
    private String username;
}
