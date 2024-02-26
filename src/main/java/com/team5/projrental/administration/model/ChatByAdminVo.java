package com.team5.projrental.administration.model;

import com.team5.projrental.administration.model.inner.ChatInfoByAdmin;
import com.team5.projrental.administration.model.inner.RefundInfoByAdmin;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChatByAdminVo {
    private Long totalChatCount;
    private List<ChatInfoByAdmin> chats;
}
