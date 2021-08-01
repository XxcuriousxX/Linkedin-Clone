package com.example.tedi_app.dto;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostResponse implements Comparable<PostResponse>{
    private Long postId;
    private String description;
    private String username;
    private int likeCount;
    private int commentCount;
    private String duration;
    private Long createdDateLong;

    @Override
    public int compareTo(PostResponse u) {
        if (getCreatedDateLong() == null || u.getCreatedDateLong() == null) {
            return 0;
        }
        return getCreatedDateLong().compareTo(u.getCreatedDateLong());
    }


}
