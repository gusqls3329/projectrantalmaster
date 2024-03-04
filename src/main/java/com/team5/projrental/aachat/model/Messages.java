package com.team5.projrental.aachat.model;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Messages {
    //메세지 관련 정보
    private Long seq;
    private String msg; // 메세지
    private LocalDateTime createdAt; // 각 seq에 대한 메세지 보낸 시간
    private Integer isMyMessage;
    // 메세지 보낸 사람 사진
}
