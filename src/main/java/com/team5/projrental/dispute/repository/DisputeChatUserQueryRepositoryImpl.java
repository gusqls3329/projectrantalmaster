package com.team5.projrental.dispute.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.projrental.entities.ChatUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.team5.projrental.entities.QChatUser.chatUser;

@Slf4j
@RequiredArgsConstructor
public class DisputeChatUserQueryRepositoryImpl implements DisputeChatUserQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public List<ChatUser> findAllLimitPage(Integer page) {

        return query.selectFrom(chatUser)
                .join(chatUser.user).fetchJoin()
                .join(chatUser.chat.product).fetchJoin()
                .fetch();

    }
}
