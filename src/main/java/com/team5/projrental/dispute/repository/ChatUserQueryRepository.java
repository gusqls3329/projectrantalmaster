package com.team5.projrental.dispute.repository;

import com.team5.projrental.entities.ChatUser;
import com.team5.projrental.entities.User;

import java.util.Optional;

public interface ChatUserQueryRepository {
    Optional<ChatUser> findByIchatAndNeUser(Long identity, User reporter);


    //아래내용들은 상원 Chat에 구현된 내용들
    Long getChatCount(Long loginedIuser);

    ChatUser delUserStatus(Long ichat, Long LoginedIuser);
}

