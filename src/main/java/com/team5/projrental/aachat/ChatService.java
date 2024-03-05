package com.team5.projrental.aachat;

import com.team5.projrental.aachat.model.ChatMsgInsDto;
import com.team5.projrental.aachat.model.ChatMsgSelVo;
import com.team5.projrental.aachat.model.ChatSelDto;
import com.team5.projrental.aachat.model.ChatSelVo;
import com.team5.projrental.aachat.repository.ChatMsgRepository;
import com.team5.projrental.aachat.repository.ChatRepository;
import com.team5.projrental.common.Const;
import com.team5.projrental.common.exception.ErrorCode;
import com.team5.projrental.common.exception.thrid.ClientException;
import com.team5.projrental.common.model.ResVo;
import com.team5.projrental.common.security.AuthenticationFacade;

import com.team5.projrental.dispute.repository.ChatUserRepository;
import com.team5.projrental.entities.Chat;
import com.team5.projrental.entities.ChatMsg;
import com.team5.projrental.entities.ChatUser;
import com.team5.projrental.entities.User;
import com.team5.projrental.entities.enums.ChatUserStatus;
import com.team5.projrental.product.thirdproj.japrepositories.product.ProductRepository;
import com.team5.projrental.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final ChatMsgRepository chatMsgRepository;
    private final ChatUserRepository chatUserRepository;
    private final ProductRepository productRepository;
    private final ChatMapper mapper;

    private final AuthenticationFacade facade;




    // 메세지 보낼 때
    @Transactional
    public Long changeUserStatus(Long ichat, Long loginedIuser) {

        // chatUser테이블의 상대유저 PK 가져옴
        ChatUser chatUser = chatUserRepository.changeUserStatus(ichat, loginedIuser);

        // 상대유저의 status상태가 DELETE라면 ACTIVE로 변경
        if (chatUser.getStatus().equals(ChatUserStatus.DELETE)) {
            chatUser.setStatus(ChatUserStatus.ACTIVE);
        }

        return chatUser.getUser().getId();
    }

    // 메세지 입력
    @Transactional
    public void saveMsg(ChatMsgInsDto dto) {
        Chat chat = chatRepository.getReferenceById(dto.getIchat());

        ChatUser findChatUser = chatMsgRepository.findByIuserAndIchat(dto.getSenderIuser(), dto.getIchat());

        ChatMsg chatMsg = new ChatMsg();
        chatMsg.setMsg(dto.getMessage());
        chatMsg.setChatUser(findChatUser);


        chatMsgRepository.save(chatMsg);
        chat.setLastMsg(dto.getMessage());
        //chatMsgRepository.updChatLastMsg(dto);
        //mapper.updChatLastMsg(dto);
    }


    //

    // 로그인한 유저의 채팅방 리스트
    public List<ChatSelVo> getRoomList(Integer page) {
        Long loginedIuser = facade.getLoginUserPk();

        // 전체 방 리스트.
        ChatSelDto dto = new ChatSelDto();
        dto.setPage(page);
        dto.setLoginedIuser(getLoginUserPk());

        List<ChatSelVo> findChatSelVo = mapper.selChatAll(dto);

        // todo 예외처리
        ChatSelVo selvo = new ChatSelVo();
        selvo.setTotalChatCount(chatUserRepository.getChatCount(loginedIuser));

        return findChatSelVo;
    }

    // 방 들어갔을때 채팅글 리스트.
    public List<ChatMsgSelVo> getChatMsgList(long ichat, Integer page) {

        // todo 예외처리
        Long loginedIuser = facade.getLoginUserPk();

        List<ChatMsgSelVo> findChatMsgSelVo = chatMsgRepository.findAllChatMsgByIchat(ichat, loginedIuser, page);
        return findChatMsgSelVo;
    }

    // 방 새로 등록하기전 SELECT 해서 기존 iproduct 및 iuser가 같은 ichat에 있으면 아래 방새로 등록되지 않고 기존 방 입장되도록 해야함


    // 채팅방 입장
    @Transactional
    public ResVo postRoom(Long targetIuser, Long iproduct) {
        Chat saveChat = Chat.builder()
                .product(productRepository.findById(iproduct).orElseThrow(() -> new ClientException(ErrorCode.NO_SUCH_PRODUCT_EX_MESSAGE,
                        "상품이 존재하지 않습니다.")))
                .build();
        User findUserMe = userRepository.findById(facade.getLoginUserPk()).orElseThrow(() -> new ClientException(ErrorCode.ILLEGAL_STATUS_EX_MESSAGE, "로그인 유저가 존재하지 않습니다."));
        User findUserTarget = userRepository.findById(targetIuser).orElseThrow(() -> new ClientException(ErrorCode.NO_SUCH_USER_EX_MESSAGE, "상대방 유저가 존재하지 않습니다."));


        // 해당 iproduct에 로그인유저와 iproduct판매자에 대한 채팅방에 입장한 유저 있는지 확인. ichat이 셀렉되면 iproduct에 대한 로그인(구매)유저 상대(판매)유저 pk가 존재함(=기존채팅방 존재)
        Long ichat = chatUserRepository.checkChatUserChatRoom(facade.getLoginUserPk(), iproduct);

        if (ichat != null) { // 채팅유저가 존재하면 리턴 2 = 입장성공
            return new ResVo(2L);

        } else {
            chatUserRepository.save(ChatUser.builder() // ichat이 존재하지 않으면 새로운 방 생성 후 "로그인(구매자)" 유저 저장
                    .chat(saveChat)
                    .user(findUserMe)
                    .status(ChatUserStatus.ACTIVE)
                    .build());

            chatUserRepository.save(ChatUser.builder() // ichat이 존재하지 않으면 새로운 방 생성 후 "상대(판매자)" 유저 저장
                    .chat(saveChat)
                    .user(findUserTarget)
                    .status(ChatUserStatus.ACTIVE)
                    .build());
        }
        return new ResVo(1L);
    }

    // 로그인한 유저의 채팅리스트 전체 카운트
    @Transactional
    public ResVo getChatCount() {

        Long counter = chatUserRepository.getChatCount(getLoginUserPk());
        return new ResVo(counter);
    }

    //채팅방 삭제(숨김)
    @Transactional
    public ResVo deleteChat(Long ichat) {
        Long loginedIuser = facade.getLoginUserPk();
        ChatUser chatUser = chatUserRepository.delUserStatus(ichat, loginedIuser);
        chatUser.setStatus(ChatUserStatus.DELETE);

        return new ResVo(Const.SUCCESS);
    }













    // ex m

    private Long getLoginUserPk() {
        return facade.getLoginUserPk();
    }

//    public List<ChatSelVo> getChatAll(ChatSelDto dto, Pageable pageable) {
//
//        Long loginUserPk = authenticationFacade.getLoginUserPk();
//        dto.setLoginedIuser(loginUserPk);
//        List<ChatSelVo> list = mapper.selChatAll(dto);
//        return list;
//    }
//
//
//    public ResVo postChatMsg(ChatMsgInsDto dto) {
//        Long loginUserPk = authenticationFacade.getLoginUserPk();
//        dto.setLoginedIuser(loginUserPk);
//        dto.setMsg(CommonUtils.ifContainsBadWordChangeThat(dto.getMsg()));
//        int istatus = mapper.delBeforeChatIstatus(dto);
//
//        CommonUtils.ifChatUserStatusThrowOrReturn(istatus);
//
//        int affectedRows = mapper.insChatMsg(dto);
//        if (affectedRows == 1) {
//            mapper.updChatLastMsg(dto);
//        }
//
//        LocalDateTime now = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String createdAt = now.format(formatter);
//
//        UserEntity otherPerson = mapper.selOtherPersonByLoginUser(dto);
//
//
//        try {
//            if (otherPerson.getFirebaseToken() != null) {
//                ChatMsgPushVo pushVo = new ChatMsgPushVo();
//                pushVo.setIchat(dto.getIchat());
//                pushVo.setSeq(dto.getSeq());
//                pushVo.setWriterIuser(dto.getLoginedIuser());
//                pushVo.setMsg(dto.getMsg());
//                pushVo.setCreatedAt(createdAt);
//
//                String body = objectMapper.writeValueAsString(pushVo);
//
//                Notification noti = Notification.builder()
//                        .setTitle("chat")
//                        .setBody(body)
//                        .build();
//
//                Message message = Message.builder()
//                        .setToken(otherPerson.getFirebaseToken())
//                        .setNotification(noti)
//                        .build();
//
//                FirebaseMessaging.getInstance().sendAsync(message);
//
//
//                // Async 비동기 - 내가 움직이는것과 상관없이 상대방도 움직일 수 있음
//                // 동기 - 턴게임(장기, 체스) 내가 움직이면 상대방이 못움직임
//                // 스레드 - 동작단위 (게임 예 : 총게임에 캐릭터 한명한명)
//                // 메인스레드는 통신하지 말것
//
//                // FirebaseMessaging fm = FirebaseMessaging.getInstance(); // 싱글톤
//                // fm.sendAsync(message);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return new ResVo((long) dto.getSeq());
//    }
//
//    //채팅방 입장시 메세지 내용 불러오기
//    public List<ChatMsgSelVo> getMsgAll(ChatMsgSelDto dto) {
//        Long loginUserPk = authenticationFacade.getLoginUserPk();
//        dto.setLoginedIuser(loginUserPk);
//
//        List<ChatMsgSelVo> list = mapper.selChatMsgAll(dto);
//        return list;
//    }
//
//    // 메세지 삭제(실제로 숨김처리)
//    public ResVo chatDelMsg(ChatMsgDelDto dto) {
//        dto.setIuser(authenticationFacade.getLoginUserPk());
//        int updAffectedRows = mapper.updChatLastMsgAfterDelByLastMsg(dto);
//        return new ResVo((long) updAffectedRows);
//    }
//
//    // 채팅 입력
//    public ChatSelVo postChat(ChatInsDto dto) {
//
//        Long loginUserPk = authenticationFacade.getLoginUserPk();
//        dto.setLoginedIuser(loginUserPk);
//
//        /*Integer isExixtChat = mapper.selChatUserCheck(dto);
//            if (isExixtChat != null) {
//            throw new BadInformationException(ErrorCode.ILLEGAL_EX_MESSAGE);
//        }*/
//
//        Integer existEnableRoom = mapper.selChatUserCheck2(dto);
//        if (existEnableRoom == null || existEnableRoom == 0) {
//            mapper.insChat(dto);
//
//
//            mapper.insChatUser(ChatUserInsDto.builder()
//                    .ichat(dto.getIchat())
//                    .iuser(loginUserPk)
//                    .build());
//
//            mapper.insChatUser(ChatUserInsDto.builder()
//                    .ichat(dto.getIchat())
//                    .iuser(dto.getOtherPersonIuser())
//                    .build());
//        }
//
//        /*UserSelDto usDto = new UserSelDto();
//        usDto.setIuser(dto.getOtherPersonIuser());*/
//
//        // 로그인 유저와 다른 유저정보 입력 오류메세지
//        if (loginUserPk == dto.getOtherPersonIuser()) {
//            throw new BadInformationException(BAD_SAME_USER_MESSAGE);
//        }
//
//        UserEntity entity = mapper.selChatUser(dto.getOtherPersonIuser());
//
//        ChatSelVo vo = new ChatSelVo();
//        vo.setIproduct(dto.getIproduct());
//        vo.setOtherPersonIuser(entity.getIuser());
//        vo.setOtherPersonNm(entity.getNick());
//        vo.setOtherPersonPic(entity.getStoredPic());
//        return vo;
//    }


}
