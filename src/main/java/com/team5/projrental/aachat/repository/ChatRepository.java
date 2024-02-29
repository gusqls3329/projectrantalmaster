package com.team5.projrental.aachat.repository;

import com.team5.projrental.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> , ChatQdslRepository {

}
