package com.team5.projrental.board.comment.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team5.projrental.common.exception.ErrorMessage;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardCommentDelDto {
    @NotNull(message = ErrorMessage.CAN_NOT_BLANK_EX_MESSAGE)
    @Min(value = 1)
    private int iboardComment;
    @JsonIgnore
    private int loginIuser;
}
