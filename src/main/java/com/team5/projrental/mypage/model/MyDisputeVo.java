package com.team5.projrental.mypage.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.team5.projrental.administration.model.inner.DisputeInfoByAdmin;
import com.team5.projrental.entities.enums.DisputeReason;
import com.team5.projrental.entities.enums.DisputeStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyDisputeVo {
    private long idispute;
    private long ireporter;

    private DisputeReason category;
    private String details;
    private LocalDate createdAt;
    private DisputeStatus status;
    private int penalty;
    private int reason;
    private int kind;
    private Long pk;

    private Long ireportedUser;
    private String reportedUserNick;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    // 제품이면 신고 당한 유저의 mainPic, 유저면 mainPic, 나머지는 null (@JoinInclude)
    private String pic;

    // 유저테이블
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nick; //닉네임

    // 상품테이블
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String tilte; //상품 이름

    // 결제테이블
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String code;

    //채팅
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lastMsg;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lastMsgAt;

    //보드
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String boardTitle;

}
