package com.team5.projrental.aachat.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.projrental.aachat.model.ChatMsgInsDto;
import com.team5.projrental.aachat.model.ChatMsgSelVo;
import com.team5.projrental.common.Const;
import com.team5.projrental.entities.ChatMsg;
import com.team5.projrental.entities.ChatUser;
import com.team5.projrental.entities.QChatMsg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.team5.projrental.entities.QChatMsg.chatMsg;
import static com.team5.projrental.entities.QChatUser.chatUser;
import static com.team5.projrental.entities.QUser.user;

@Slf4j
@RequiredArgsConstructor
public class ChatMsgQdslRepositoryImpl implements ChatMsgQdslRepository {
    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public ChatUser findByIuserAndIchat(Long senderIuser, Long ichat) {


        return jpaQueryFactory.selectFrom(chatUser)
                .where(chatUser.chat.id.eq(ichat).and(chatUser.user.id.eq(senderIuser)))
                .fetchOne();
    }

    @Override
    public List<ChatMsgSelVo> findAllChatMsgByIchat(long ichat, Long loginedIuser, Integer page) {

        return jpaQueryFactory.select(Projections.fields(ChatMsgSelVo.class,
                        user.id.as("senderIuser"),
                        user.nick.as("senderNick"),
                        user.baseUser.storedPic.as("senderPic"),
                        chatMsg.msg,
                        chatMsg.createdAt))
                .from(chatMsg)
                .join(chatMsg.chatUser)
                .join(user).on(chatMsg.chatUser.user.eq(user))
                .where(chatMsg.chatUser.chat.id.eq(ichat))
                .orderBy(chatMsg.createdAt.desc())
                .offset(page)
                .limit(Const.CHAT_MSG_PER_PAGE)
                .fetch();

    }


    //상원
    @Override
    public Long updChatLastMsg(ChatMsgInsDto dto) {
        QChatMsg qChatMsg = QChatMsg.chatMsg;

        long updatedCount = jpaQueryFactory
                .update(qChatMsg)
                .set(qChatMsg.msg, dto.getMessage())
                .where(qChatMsg.id.eq(dto.getIchat()))
                .execute();

        if (updatedCount != 1) {
            throw new RuntimeException("Failed to update chat last message for ichat: " + dto.getIchat());
        }

        return dto.getIchat();
    }
}
