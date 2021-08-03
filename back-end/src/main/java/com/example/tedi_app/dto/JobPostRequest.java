package com.example.tedi_app.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JobPostRequest {
    private String authorUsername;
    private String title;
    private String location;
    private String employmentType; // full-time or part-time, temporary
    private String details;
    private String requiredSkills; // "skill1,skill2,...,skillN"
    private String keywords; // "keyword1,ketword2,...,keywordN"
}