package com.team5.projrental.administration.model.inner;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RefundInfoByAdmin {
    private Long irefund;
    private Long iuser;
    private String uid;
    private String nick;
    private Integer amount;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long iadmin;
    private String adminUid;
}
