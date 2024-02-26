package com.team5.projrental.dispute.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.projrental.entities.ChatUser;
import com.team5.projrental.entities.User;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.team5.projrental.entities.QChatUser.chatUser;

@RequiredArgsConstructor
public class ChatUserQueryRepositoryImpl implements ChatUserQueryRepository{


    private final JPAQueryFactory query;

    @Override
    public Optional<ChatUser> findByIchatAndNeUser(Long identity, User reporter) {

        return Optional.ofNullable(query.selectFrom(chatUser)
                .join(chatUser.user).fetchJoin()
                .where(chatUser.chat.id.eq(identity).and(chatUser.user.ne(reporter)))
                .fetchOne());
    }
}
