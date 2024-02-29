package com.team5.projrental.administration.model.inner;

import com.team5.projrental.entities.enums.DisputeReason;
import com.team5.projrental.entities.enums.DisputeStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class DisputeInfoByAdmin {

    private Long idispute;
    private Long ireportedUser;
    private Long ireporter;
    private String uid;
    private String nick;
    private String category;
    private LocalDateTime createdAt;
    private String status;
    private String detail;
    private Integer penalty;
    private String kind;
    private String pk;

}
