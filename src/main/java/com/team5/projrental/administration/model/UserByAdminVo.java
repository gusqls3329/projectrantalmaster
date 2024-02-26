package com.team5.projrental.administration.model;

import com.team5.projrental.administration.model.inner.UserInfoByAdmin;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class UserByAdminVo {

    private Long totalUserCount;
    private List<UserInfoByAdmin> users;

}
