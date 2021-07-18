package com.example.tedi_app.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Long id;
    private String url;
    private String description;
    private String username;
    private int like_count;
    private int comment_count;
    private String duration;
}
