package com.team5.projrental.dispute.repository;


import com.team5.projrental.entities.ChatUser;
import com.team5.projrental.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long>, ChatUserQueryRepository {

}
