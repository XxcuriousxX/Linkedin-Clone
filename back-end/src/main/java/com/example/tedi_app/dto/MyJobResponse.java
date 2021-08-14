package com.example.tedi_app.dto;


import com.example.tedi_app.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class MyJobResponse {
    private Long jobpostid;
    private String title;
    private int views;
    private ArrayList<User> user_list;

    public MyJobResponse(Long jobpostid, String title, ArrayList<User> user_list,int views) {
        this.jobpostid = jobpostid;
        this.title = title;
        this.user_list = new ArrayList<>(user_list);
        this.views = views;
    }
}
