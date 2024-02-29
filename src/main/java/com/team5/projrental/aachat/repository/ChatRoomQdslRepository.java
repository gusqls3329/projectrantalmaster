package com.team5.projrental.aachat.repository;


import com.team5.projrental.aachat.model.ChatSelVo;

import java.util.List;

public interface ChatRoomQdslRepository {

    List<ChatSelVo> selChatList(long iuser, Integer page);
}
