package com.team5.projrental.administration.model.inner;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class DisputeInfoByAdmin {

    private Long idispute;

    private Long ireportedUser;
    private String reportedUserUid;
    private String reportedUserNick;

    private Long ireporter;
    private String reporterUid;
    private String reporterNick;



    private String category;
    private LocalDateTime createdAt;
    private String status;
    private String detail;
    private Integer penalty;
    private String kind;
    private String pk;

}
