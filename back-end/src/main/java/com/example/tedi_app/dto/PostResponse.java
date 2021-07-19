package com.example.tedi_app.dto;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostResponse {
    private Long postId;
    private String description;
    private String username;
    private int likeCount;
    private int commentCount;
    private String duration;
}
