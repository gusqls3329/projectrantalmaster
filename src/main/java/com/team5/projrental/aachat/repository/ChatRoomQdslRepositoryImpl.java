package com.team5.projrental.aachat.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.projrental.aachat.model.ChatSelVo;
import com.team5.projrental.common.Const;
import com.team5.projrental.entities.enums.ChatUserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.team5.projrental.entities.QChatUser.chatUser;


@Slf4j
@RequiredArgsConstructor
public class ChatRoomQdslRepositoryImpl implements ChatRoomQdslRepository {
    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<ChatSelVo> selChatList(long loginIuser, Integer page) {

        return jpaQueryFactory.select(Projections.bean(ChatSelVo.class
                        , chatUser.id.as("ichat")
                        , chatUser.chat.product.id
                        , chatUser.user.baseUser.storedPic.as("otherPersonPic")
                        , chatUser.chat.lastMsg
                        , chatUser.chat.lastMsgAt
                        , chatUser.user.id.as("otherPersonIuser")
                        , chatUser.user.nick.as("otherPersonNm")
                        , chatUser.chat.product.storedPic.as("prodPic")
                        , chatUser.chat.createdAt
                ))
                .from(chatUser)
                .join(chatUser.chat).fetchJoin()
                .join(chatUser.chat.product).fetchJoin()
                .where(chatUser.user.id.ne(loginIuser)
                        .and(chatUser.status.eq(ChatUserStatus.ACTIVE)))
                .offset(page)
                .limit(Const.CHAT_LIST_PER_PAGE)
                .orderBy(chatUser.chat.lastMsgAt.desc())
                .fetch();


    }


}
