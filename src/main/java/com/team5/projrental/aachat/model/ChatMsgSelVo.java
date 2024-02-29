package com.team5.projrental.aachat.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMsgSelVo {
    private Long senderIuser; // 작성자 PK
    private String senderNick;
    private String senderPic; // 메세지 보낸 사람 사진
    private String msg; // 메세지
    private LocalDateTime createdAt; // 각 seq에 대한 메세지 보낸 시간




}
