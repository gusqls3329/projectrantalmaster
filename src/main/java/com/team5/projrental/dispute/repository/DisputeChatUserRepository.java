package com.team5.projrental.dispute.repository;

import com.team5.projrental.entities.ChatUser;
import com.team5.projrental.entities.DisputeChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DisputeChatUserRepository extends JpaRepository<DisputeChatUser, Long>,  DisputeChatUserQueryRepository{


}
