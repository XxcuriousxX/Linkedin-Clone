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
public class ActionResponse {
    private String fromUsername;
//    private String toUsername;
    private Integer actionType = -1;
    private Long postId;  // will be used by frontend to define the link to the full-post view
    private String timeCreated;
    private String text;

    public ActionResponse(String fromUsername, Integer actionType, Long postId, Instant timeCreated) {
        this.fromUsername = fromUsername;
//        this.toUsername = toUsername;
        this.actionType = actionType;
        this.postId = postId;
        Date d = Date.from(timeCreated);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd/mm/yyyy");
        this.timeCreated = formatter.format(d);
        if (actionType == 1)
            this.text = fromUsername + " liked your post";
        else if (actionType == 2)
            this.text = fromUsername + " made a comment on your post";
    }
}
