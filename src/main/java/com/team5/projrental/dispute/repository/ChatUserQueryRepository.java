package com.team5.projrental.dispute.repository;

import com.team5.projrental.entities.ChatUser;
import com.team5.projrental.entities.User;

import java.util.Optional;

public interface ChatUserQueryRepository {
    Optional<ChatUser> findByIchatAndNeUser(Long identity, User reporter);
}
