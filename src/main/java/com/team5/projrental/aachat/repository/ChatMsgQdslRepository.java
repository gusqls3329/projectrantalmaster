package com.team5.projrental.aachat.repository;

import com.team5.projrental.aachat.model.ChatMsgSelVo;
import com.team5.projrental.entities.ChatUser;

import java.util.List;

public interface ChatMsgQdslRepository {
    ChatUser findByIuserAndIchat(Long senderIuser, Long ichat);

    List<ChatMsgSelVo> findAllChatMsgByIchat(long ichat, Integer page);

}
