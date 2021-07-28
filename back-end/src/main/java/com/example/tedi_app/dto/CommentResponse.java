package com.example.tedi_app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {
    private Long postId;
    private String username;
    private String text;
    private String createdDate;

    public CommentResponse(Long postId, String username, String txt, Instant dateInstant) {
        this.postId = postId;
        this.username = username;
        this.text = txt;
        Date d = Date.from(dateInstant);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd/mm/yyyy");
        this.createdDate = formatter.format(d);
    }
}
