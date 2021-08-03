
package com.example.tedi_app.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JobPostResponse {
    private Long jobPostId;
    private String title;
    private String location;
    private String keywords;
    private String employmentType;
    private String details;
    private String requiredSkills;
    
}