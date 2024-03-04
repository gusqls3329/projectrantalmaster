package com.team5.projrental.dispute.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.projrental.aachat.model.ChatSelVo;
import com.team5.projrental.entities.Chat;
import com.team5.projrental.entities.ChatUser;
import com.team5.projrental.entities.User;
import com.team5.projrental.entities.enums.ChatUserStatus;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.team5.projrental.entities.QChat.chat;
import static com.team5.projrental.entities.QChatUser.chatUser;
import static com.team5.projrental.entities.QProduct.product;

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

    @Override
    public Long getChatCount(Long loginedIuser) {

        return query.select(chatUser.count())
                .from(chatUser)
                .where(chatUser.user.id.eq(loginedIuser).and(chatUser.status.eq(ChatUserStatus.ACTIVE)))
                .fetchOne();
    }


    // 유저 숨김(삭제)
    @Override
    public ChatUser delUserStatus(Long ichat, Long loginedIuser) {

        return query.selectFrom(chatUser)
                .where(chatUser.user.id.eq(loginedIuser).and(chatUser.chat.id.eq(ichat).and(chatUser.status.eq(ChatUserStatus.ACTIVE))))
                .fetchOne();
    }


    //여기 작성해야함 상대유저 pk값 얻어와야 delete이면 ACTIVE로 바꿔야함
    @Override // 상대 유저pk값 셀렉해야함
    public ChatUser changeUserStatus(Long ichat, Long loginedIuser) {
        return query.selectFrom(chatUser)
                .where(chatUser.chat.id.eq(ichat).and(chatUser.user.id.ne(loginedIuser)))
                .fetchOne();
    }


    // 유저가 입력한 iproduct에 ichat이 존재하면 기존채팅방에서 채팅하도록 ichat이 존재하지않으면 service에서 채팅방 생성 및 유저 넣음
    @Override
    public Long checkChatUserChatRoom(Long loginedIuser, Long iproduct){

        return query.select(chatUser.chat.id)
                .from(product)
                .join(chat)
                .on(product.id.eq(chat.product.id))
                .join(chatUser)
                .on(chatUser.chat.id.eq(chat.id))
                .where(chatUser.user.id.eq(loginedIuser).and(chat.product.id.eq(iproduct)).and(chatUser.status.eq(ChatUserStatus.ACTIVE)))
                .fetchOne();

    }
}
