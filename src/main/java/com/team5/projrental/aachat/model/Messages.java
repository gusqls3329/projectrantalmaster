package com.team5.projrental.aachat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Messages {
    //메세지 관련 정보

    @JsonIgnore
    private Long iuser;
    private Long asc;
    private String msg; // 메세지
    private LocalDateTime createdAt; // 각 seq에 대한 메세지 보낸 시간
    private int isMyMessage;
    // 메세지 보낸 사람 사진
}
