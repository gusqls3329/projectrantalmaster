package com.team5.projrental.mypage.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team5.projrental.common.exception.ErrorMessage;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class ProductListDto {
    @Range(min = 1, message = ErrorMessage.ILLEGAL_RANGE_EX_MESSAGE)
    private int page;//페이지

    @JsonIgnore
    private Long iuser; // 로그인 유저정보가 ibuyer에 조건으로 들어가면 빌린내역 seller에 들어가면 빌려준내역

    @Range(min = 1, message = ErrorMessage.ILLEGAL_RANGE_EX_MESSAGE)
    private Long targetIuser;

    private int startIdx;// 시작 인덱스 넘버

    @JsonIgnore
    private int rowCount = 6;//페이지 당 일지 수


    public void setPage(int page){
        this.startIdx= (page-1)*this.rowCount;
    }
}
