package com.team5.projrental.administration.repository;

import com.team5.projrental.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminChatRepository extends JpaRepository<Chat, Long> {
}
