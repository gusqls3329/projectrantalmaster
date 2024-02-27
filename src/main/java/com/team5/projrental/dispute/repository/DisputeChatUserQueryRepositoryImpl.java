package com.team5.projrental.dispute.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.projrental.common.Const;
import com.team5.projrental.entities.ChatUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.team5.projrental.entities.QChatUser.chatUser;
import static com.team5.projrental.entities.QProduct.product;

@Slf4j
@RequiredArgsConstructor
public class DisputeChatUserQueryRepositoryImpl implements DisputeChatUserQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public List<ChatUser> findAllLimitPage(Integer page) {

        return query.selectFrom(chatUser)
                .join(chatUser.user).fetchJoin()
                .join(product).on(chatUser.chat.product.eq(product)).fetchJoin()
                .offset(page)
                .limit(Const.ADMIN_PER_PAGE)
                .orderBy(chatUser.chat.id.desc())
                .fetch();

    }

    @Override
    public Long totalCountByOptions() {
        return query.select(chatUser.count())
                .from()
                .join(chatUser.user).fetchJoin()
                .join(product).on(chatUser.chat.product.eq(product)).fetchJoin()
                .fetchOne();
    }
}
