package com.team5.projrental.administration.model.inner;

import com.team5.projrental.entities.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserInfoByAdmin {

    private Long iuser;
    private String uid;
    private String nick;
    private LocalDateTime createdAt;
    private String email;
    private Integer penalty;
    private UserStatus status;

}
