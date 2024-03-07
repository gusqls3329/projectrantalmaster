package com.team5.projrental.aachat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

// 채팅방 리스트
@Data
public class ChatSelVo {


    private Long totalChatCount;
    // 채팅방 전체 수
    private int ichat; // 채팅방 PK
    private String lastMsg; // 최근 메세지 내용
    private String lastMsgAt; // 최근 메세지 발송시간
    private String istatus; // 채팅방 상태

    private int iproduct; // 제품PK
    private String title; // 제품 타이틀

    private String otherPersonNm; // 상대방 유저 이름
    private String otherPersonPic; // 상대방 유저 사진
    private String otherPersonIuser;
    private String prodPic; // 제품 대표사진

    @JsonIgnore
    private int existEnableRoom;









}
