package com.team5.projrental.board.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team5.projrental.common.exception.ErrorMessage;
import com.team5.projrental.entities.enums.BoardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.annotation.Nullable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardListSelDto {


    @JsonIgnore
    private long loginIuser;

    private Integer type; // 1:제목 , 2: 제목+내용, 3:닉네임

    private String search; // 검색어

    private int sort; // 0:최신순 ,1:좋아요순, 2:조회수순

    @JsonIgnore
    private String status;

    @Range(min = 1, message = ErrorMessage.ILLEGAL_RANGE_EX_MESSAGE)
    private int page;

    @JsonIgnore
    private int startIdx;

    @JsonIgnore
    //@Builder.Default
    private int rowCount = 12;

    public void setPage(int page) {
        this.startIdx = (page-1) * this.rowCount;
    }

}
