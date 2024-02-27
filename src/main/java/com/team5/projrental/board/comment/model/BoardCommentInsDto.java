package com.team5.projrental.board.comment.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardCommentInsDto {
    @JsonIgnore
    private long iboardComment;

    private long loginIuser;

    private long iboard;

    private String comment;
}
