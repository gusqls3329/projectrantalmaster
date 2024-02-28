package com.team5.projrental.board.comment.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardCommentInsDto {
    @JsonIgnore
    private long iboardComment;
    @JsonIgnore
    private long loginIuser;
    @JsonIgnore
    private LocalDateTime createdAt = LocalDateTime.now();

    private long iboard;

    private String comment;
}
