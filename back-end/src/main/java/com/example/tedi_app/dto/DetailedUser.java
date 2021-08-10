package com.example.tedi_app.dto;

import com.example.tedi_app.model.Personalinfo;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class DetailedUser {  // data to be sent to frontend .used when exporting data
    private Long userId;
    private String username;
    private String email;
    private String phone;
    private String first_name;
    private String last_name;
    private String company_name;

    private String work_desc = "";
    private String stud_desc = "";
    private String abilities_desc = "";


    private List<PostResponse> posts;
    private List<JobPostResponse> jobPosts;
    private List<CommentResponse> comments;
    private List<VoteData> likes;
    private List<String> connectedWith;

}
