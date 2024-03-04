package com.team5.projrental.aachat;

import com.team5.projrental.aachat.model.ChatMsgInsDto;
import com.team5.projrental.aachat.model.ChatMsgSelVo;
import com.team5.projrental.aachat.model.ChatSelVo;
import com.team5.projrental.common.Const;
import com.team5.projrental.common.model.ResVo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService service;

    //로그인 유저의 모든 채팅방 카운트
    @GetMapping("count")
    @Operation(summary = "채팅방 수", description = "로그인 유저가 참여중인 채팅방 수")
    public ResVo getChatCount() {
        return service.getChatCount();
    }

    //로그인 유저의 채팅 리스트
    @GetMapping()
    @Operation(summary = "채팅방 리스트", description = "로그인 유저가 참여중인 채팅방 리스트")
    public List<ChatSelVo> getChatAll(@RequestParam(defaultValue = "1") Integer page) {
        return service.getRoomList(page);
    }

    @GetMapping("room/{ichat}")
    @Operation(summary = "채팅방 메세지 리스트", description = "로그인 유저가 입장한 채팅방의 메세지 리스트")
    public List<ChatMsgSelVo> getMessageAll(@PathVariable("ichat") Long ichat, @RequestParam(defaultValue = "1") Integer page) {

        return service.getChatMsgList(ichat, (page - 1) * Const.CHAT_MSG_PER_PAGE);
    }

    // 채팅방 입장
    @PostMapping("room/{target-iuser}")
    @Operation(summary = "채팅방 입장", description = "로그인 한 유저가 대화버튼 누를 경우 채팅방 생성 및 유저 입장됨")
    public ResVo postRoom(@PathVariable("target-iuser") Long targetIuser, @RequestParam Long iproduct) {



        return service.postRoom(targetIuser, iproduct);
    }

    // 채팅방 삭제
    @DeleteMapping("{ichat}")
    @Operation(summary = "채팅방 삭제(숨김)", description = "로그인한 유저의 입력된 채팅방 삭제(숨김)처리")
    public ResVo delChat(@PathVariable("ichat")Long ichat) {
        return service.deleteChat(ichat);
    }


    // 이부분은 ChatMessageController에 구현되어있어보임 확인필요 -상원
    /*@PostMapping("send/{ichat-room}")
    @Operation(summary = "채팅 메세지 입력", description = ("채팅방 메세지 입력"))
    public ResVo chatSend(@PathVariable("ichat-room") Long ichatRoom, @PathVariable("tartget-iuser") Long targetIuser, ChatMsgInsDto dto) {

        return null;
    }*/



}

