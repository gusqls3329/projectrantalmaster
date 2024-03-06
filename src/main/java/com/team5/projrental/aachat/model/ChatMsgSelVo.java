package com.team5.projrental.aachat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team5.projrental.entities.mappedsuper.CreatedAt;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChatMsgSelVo {

    @JsonIgnore
    private Long ichatMsg;
    //제품 관련 정보
    private Long iproduct;
    private String title;
    private String productMainPic;
    private Integer rentalPrice;

    private Long isender;
    private String senderNick;
    private String senderPic;

    private String msg;
    private LocalDateTime createdAt;
    //



}
