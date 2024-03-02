package com.team5.projrental.aachat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

// 채팅방 리스트
@Data
public class ChatSelVo {
    private int ichat; // 채팅방 PK
    private String lastMsg; // 최근 메세지 내용
    private String lastMsgAt; // 최근 메세지 발송시간
    private String otherPersonNm; // 상대방 유저 이름
    private String otherPersonPic; // 상대방 유저 사진
    private String prodPic; // 제품 대표사진

    @JsonIgnore
    private int existEnableRoom;

}