
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
}