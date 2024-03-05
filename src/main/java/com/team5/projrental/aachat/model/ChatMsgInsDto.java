package com.team5.projrental.aachat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChatMsgInsDto {
    @JsonIgnore
    private Long ichat; // 채팅방 고유 번호(채팅방 PK)

    @JsonIgnore
    private Long seq; // 각 채팅방의 채팅고유 번호(채팅 PK)

    private Long senderIuser; // 로그인 유저 PK
//    @JsonIgnore
//    private String senderNick;

    private Long receiveIuser; //상대방 PK

    @NotBlank
    private String message; // 전송할 메세지 내용


    //@NotNull // null은 안됨
    //@NotBlank // 스페이스바도 안됨
    //@NotEmpty // null아니면서 빈문자("") 까지 안됨
}
