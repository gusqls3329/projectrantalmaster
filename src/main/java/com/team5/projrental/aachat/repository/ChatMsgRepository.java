package com.team5.projrental.aachat.repository;

import com.team5.projrental.entities.ChatMsg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMsgRepository extends JpaRepository<ChatMsg, Long>, ChatMsgQdslRepository {

}
