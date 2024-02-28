package com.team5.projrental.mypage.model;

import com.team5.projrental.administration.model.inner.DisputeInfoByAdmin;
import com.team5.projrental.entities.enums.DisputeReason;
import com.team5.projrental.entities.enums.DisputeStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class MyDisputeVo {
    private long idispute;
    private long ireporter;
    private DisputeReason category;
    private String details;
    private long penality;
    private LocalDate createdAt;
    private DisputeStatus status;
    private int kind;
    private Long identity;

    // 유저테이블
    private long ireportedUser;
    private String nick;
    private String profPic; //프로필 사진

    // 상품테이블
    private Long iproduct;
    private String tilte; //상품 이름
    private String prodPic; //상품 사진

    // 상품테이블
    private long ipayment;
    private String code;

    //채팅
    private long ichat;
    private String lastMsg;
    private String lastMsgAt;

    //보드
    private long iboard;
    private String boardTitle;

}
