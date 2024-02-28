package com.team5.projrental.dispute.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.projrental.common.Const;
import com.team5.projrental.entities.ChatUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.team5.projrental.entities.QChat.chat;
import static com.team5.projrental.entities.QChatUser.chatUser;
import static com.team5.projrental.entities.QDisputeChatUser.disputeChatUser;
import static com.team5.projrental.entities.QProduct.product;

@Slf4j
@RequiredArgsConstructor
public class DisputeChatUserQueryRepositoryImpl implements DisputeChatUserQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public List<ChatUser> findAllLimitPage(Integer page) {

        return query.select(disputeChatUser.chatUser)
                .from(disputeChatUser)
                .join(disputeChatUser.chatUser)
                .join(disputeChatUser.chatUser.user)
                .join(disputeChatUser.chatUser.chat)
                .join(product).on(chat.product.eq(product))
                .offset(page)
                .limit(Const.ADMIN_PER_PAGE)
                .orderBy(chatUser.chat.id.desc())
                .fetch();

    }

    @Override
    public Long totalCountByOptions() {
        return query.select(disputeChatUser.count())
                .from(disputeChatUser)
                .join(disputeChatUser.chatUser)
                .join(disputeChatUser.chatUser.user)
                .join(disputeChatUser.chatUser.chat)
                .join(product).on(chat.product.eq(product))
                .fetchOne();
    }
}
