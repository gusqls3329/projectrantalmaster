package com.team5.projrental.administration.repository;

import com.team5.projrental.entities.User;
import com.team5.projrental.entities.enums.UserStatus;

import java.util.List;

public interface AdminUserQueryRepository {
    List<User> findUserByOptions(Integer page, Integer searchType, String search, UserStatus status);

    Long totalCountByOptions(Integer searchType, String search, UserStatus status);

}
