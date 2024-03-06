package com.team5.projrental.aachat.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.projrental.aachat.model.ChatMsgInsDto;
import com.team5.projrental.aachat.model.ChatMsgSelVo;
import com.team5.projrental.aachat.model.Messages;
import com.team5.projrental.common.Const;
import com.team5.projrental.entities.ChatMsg;
import com.team5.projrental.entities.ChatUser;
import com.team5.projrental.entities.QChatMsg;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

import static com.team5.projrental.entities.QChat.chat;
import static com.team5.projrental.entities.QChatMsg.chatMsg;
import static com.team5.projrental.entities.QChatUser.chatUser;
import static com.team5.projrental.entities.QUser.user;
import static com.team5.projrental.entities.QProduct.product;

@Slf4j
@RequiredArgsConstructor
public class ChatMsgQdslRepositoryImpl implements ChatMsgQdslRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager em;


    @Override
    public ChatUser findByIuserAndIchat(Long senderIuser, Long ichat) {


        return jpaQueryFactory.selectFrom(chatUser)
                .where(chatUser.chat.id.eq(ichat).and(chatUser.user.id.eq(senderIuser)))
                .fetchOne();
    }

    @Override
    public List<ChatMsgSelVo> findAllChatMsgByIchat(long ichat, Long loginedIuser, Integer page) {


        return null;

    }


    //상원
    @Override
    public Long updChatLastMsg(ChatMsgInsDto dto) {


        long updatedCount = jpaQueryFactory
                .update(chat)
                .set(chat.lastMsg, dto.getMessage())
                .where(chat.id.eq(dto.getIchat()))
                .execute();

        if (updatedCount != 1) {
            throw new RuntimeException("Failed to update chat last message for ichat: " + dto.getIchat());
        }

        return dto.getIchat();
    }

    @Override
    public List<ChatMsgSelVo> findBothUsersMsges(long ichat, Integer page) {

        return jpaQueryFactory.select(Projections.bean(ChatMsgSelVo.class,
                        chatMsg.id.as("ichatMsg"),
                        product.id.as("iproduct"),
                        product.title,
                        product.storedPic.as("productMainPic"),
                        product.rentalPrice,
                        user.id.as("isender"),
                        user.nick.as("senderNick"),
                        user.baseUser.storedPic.as("senderPic"),
                        chatMsg.msg,
                        chatMsg.createdAt
                )).from(chatMsg)
                .join(chatMsg.chatUser)
                .join(user).on(chatMsg.chatUser.user.id.eq(user.id))
                .join(chat).on(chatMsg.chatUser.chat.id.eq(chat.id))
                .join(product).on(chat.product.id.eq(product.id))
//                .join(chatUser).on(chat.id.eq(chatUser.chat.id))
////                .join(user).on(chatUser.user.id.eq(user.id))
//                .join(chatUser.user)
////                .join(chatMsg).on(chatUser.id.eq(chatMsg.chatUser.id))
//                .join(chatMsg.chatUser)
////                .join(product).on(chat.product.id.eq(product.id))
//                .join(chatMsg.chatUser.chat.product)
                .where(chat.id.eq(ichat))
                .offset(page)
                .limit(30)
                .orderBy(chatMsg.id.desc())
                .where(chat.id.eq(ichat))
                .fetch()
                .stream()
                .sorted((f, s) -> -((int) (f.getIchatMsg() - s.getIchatMsg())))
                .toList();

//        return jpaQueryFactory.select(Projections.bean(Messages.class,
//                chatMsg.id.as("asc"),
//                chatMsg.chatUser.user.id.as("iuser"),
//                chatMsg.msg.as("msg"),
//                chatMsg.createdAt
//                )).from(chatMsg)
//                .join(chatMsg.chatUser)
//                .join(chatMsg.chatUser.chat)
//                .offset(page)
//                .limit(30)
//                .orderBy(chatMsg.id.desc())
//                .where(chatMsg.chatUser.chat.id.eq(ichat))
//                .fetch()
//
//                .stream().map(m -> {
//                    if (Objects.equals(m.getIuser(), loginedIuser)) {
//                        m.setIsMyMessage(1);
//                    }
//                    return m;
//                })
//                .sorted((f, s) -> -((int) (f.getAsc() - s.getAsc())))
//                .toList();
//


    }
}
