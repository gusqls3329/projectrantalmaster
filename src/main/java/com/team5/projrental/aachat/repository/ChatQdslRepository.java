package com.team5.projrental.aachat.repository;

public interface ChatQdslRepository {
    Long selChatMsg(long ichat, long iuser);

    Long selOtherPersonIuser(Long loginedIuser, Long ichat);
}
